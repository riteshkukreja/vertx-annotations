package com.github.riteshkukreja.generators;

import com.github.riteshkukreja.annotations.http.Get;
import com.github.riteshkukreja.annotations.http.HttpVertical;
import com.github.riteshkukreja.annotations.http.Post;
import com.github.riteshkukreja.modal.GeneratorItem;
import com.github.riteshkukreja.modal.MethodType;
import com.github.riteshkukreja.modal.Path;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.github.riteshkukreja.annotations.http.HttpVertical")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class HttpVerticalGenerator extends AbstractProcessor {

    private Map<Integer, Map<Path, GeneratorItem>> pathsMap = new HashMap<>();

    private final String genPackage = "gen.verticals";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {

            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element classes : annotatedElements) {
                if (!verifyAndAddPathForClass(classes))
                    return false;
            }

            generateSourceModalForVertical();
        }

        return true;
    }

    private boolean hasValidAnnotations(Element element) {
        return containsGetAnnotations(element) || containsPostAnnotations(element);
    }

    private boolean verifyAndAddPathForClass(Element element) {
        HttpVertical httpVertical = element.getAnnotation(HttpVertical.class);
        Map<Path, GeneratorItem> map = new HashMap<>();

        String path = httpVertical.value();
        int port = httpVertical.port();

        final String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
        final String parentName = element.getSimpleName().toString();

        Function<String, GeneratorItem> itemGenerator =
                name -> new GeneratorItem(parentName, name, packageName);

        if (!pathsMap.containsKey(port)) {
            pathsMap.put(port, map);
        }

        List<? extends Element> collectedMethods = element.getEnclosedElements()
                .stream()
                .filter(a -> a.getKind() == ElementKind.METHOD)
                .filter(this::hasValidAnnotations)
                .collect(Collectors.toList());

        for (Element method : collectedMethods) {
            if (!verifyAndAddPathForMethod(method, path, map, itemGenerator))
                return false;
        }

        return true;
    }

    private String getClassFullName(GeneratorItem item) {
        return item.getPackageName() +
                "." +
                item.getParentName();
    }

    private boolean verifyAndAddPathForMethod(
            Element method,
            String parentPath,
            Map<Path, GeneratorItem> map,
            Function<String, GeneratorItem> itemGenerator) {

        Get get = method.getAnnotation(Get.class);
        Post post = method.getAnnotation(Post.class);

        GeneratorItem generatorItem = itemGenerator.apply(method.getSimpleName().toString());

        if (get != null) {
            String path = parentPath.trim().concat(get.value()).replaceAll("[/]+", "/");
            Path item = new Path(path, MethodType.GET);

            if (map.containsKey(item))
                return false;

            map.put(item, generatorItem);
        }

        if (post != null) {
            String path = parentPath.trim().concat(post.value()).replaceAll("[/]+", "/");
            Path item = new Path(path, MethodType.POST);

            if (map.containsKey(item))
                return false;

            map.put(item, generatorItem);
        }

        return true;
    }

    private void generateSourceModalForVertical() {
        pathsMap.forEach((port, element) -> {
            String className = "HttpVertical" + String.valueOf(port);
            try {
                generateHttpVertical(className, element, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void generateHttpVertical(String className, Map<Path, GeneratorItem> map, int port) throws IOException {
        JavaFileObject builderFile = processingEnv.getFiler()
                .createSourceFile(genPackage + "." + className);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + genPackage + ";");
            out.println("");
            out.println("import io.vertx.core.AbstractVerticle;");
            out.println("import io.vertx.ext.web.Router;");
            out.println("import com.github.riteshkukreja.annotations.GeneratedVertical;");

            out.println("");
            out.println("");

            out.println("@GeneratedVertical");
            out.println("public class " + className + " extends AbstractVerticle {");

            out.println("");

            out.println(" @Override");
            out.println(" public void start() {");
            out.println("   vertx.createHttpServer()");
            out.println("     .requestHandler(getRoutes()::accept)");
            out.println("     .listen(" + port + ");");
            out.println("   }");

            out.println("");
            out.println("");

            /** Router code **/
            out.println("   public Router getRoutes() {");
            out.println("     Router router = Router.router(vertx);");

            map.entrySet().stream()
                    .map(entry ->
                            "{\n" +
                                    getClassFullName(entry.getValue()) + " obj = new " + getClassFullName(entry.getValue()) + "(vertx);\n" +
                                    "     router." + entry.getKey().getMethod().getValue() + "(\"" +
                                    entry.getKey().getPath() +
                                    "\").handler(obj::" +
                                    entry.getValue().getName() +
                                    ");\n" +
                                    "}")

                    .forEach(entry -> out.println(entry));

            out.println("     return router;");
            out.println("   }");

            out.println("}");
        }
    }

    private boolean containsGetAnnotations(Element element) {
        return element.getAnnotation(Get.class) != null;
    }

    private boolean containsPostAnnotations(Element element) {
        return element.getAnnotation(Post.class) != null;
    }
}
