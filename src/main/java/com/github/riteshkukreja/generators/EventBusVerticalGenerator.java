package com.github.riteshkukreja.generators;

import com.github.riteshkukreja.annotations.eventbus.MessageHandler;
import com.github.riteshkukreja.modal.GeneratorItem;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("com.github.riteshkukreja.annotations.eventbus.MessageHandler")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EventBusVerticalGenerator extends AbstractProcessor {

    private Map<String, GeneratorItem> pathsMap = new HashMap<>();

    private final String genPackage = "gen.verticals";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {

            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element element : annotatedElements) {
                String name = element.getSimpleName().toString();
                String parentName = element.getEnclosingElement().getSimpleName().toString();
                String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();

                MessageHandler messageHandler = element.getAnnotation(MessageHandler.class);
                if (messageHandler == null) return false;

                String path = messageHandler.value();

                if (pathsMap.containsKey(path)) {
                    return false;
                } else {
                    pathsMap.put(path, new GeneratorItem(parentName, name, packageName));
                }
            }

            generateSourceModalForVertical();
        }

        return true;
    }


    private boolean generateSourceModalForVertical() {
        int counter = 0;

        Set<String> keys = pathsMap.keySet();
        for (String key : keys) {
            GeneratorItem item = pathsMap.get(key);

            String className = "EventBusVertical" + String.valueOf(counter++);
            try {
                generateEventBusVertical(className, item, key);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;

    }

    private String getClassFullName(GeneratorItem item) {
        return item.getPackageName() +
                "." +
                item.getParentName();
    }

    private void generateEventBusVertical(String className, GeneratorItem item, String path) throws IOException {
        JavaFileObject builderFile = processingEnv.getFiler()
                .createSourceFile(genPackage + "." + className);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + genPackage + ";");
            out.println("");
            out.println("import io.vertx.core.AbstractVerticle;");
            out.println("import com.github.riteshkukreja.annotations.GeneratedVertical;");

            out.println("");
            out.println("");

            out.println("@GeneratedVertical");
            out.println("public class " + className + " extends AbstractVerticle {");

            out.println("");

            out.println(" @Override");
            out.println(" public void start() {");
            out.println(getClassFullName(item) + " obj = new " + getClassFullName(item) + "(vertx);");
            out.println(
                    "   vertx.eventBus().consumer(\"" +
                            path +
                            "\", obj::" +
                            item.getName() +
                            ");");

            out.println("   }");

            out.println("");
            out.println("");

            out.println("}");
        }
    }
}
