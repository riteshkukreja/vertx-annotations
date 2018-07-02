package com.github.riteshkukreja.generators;

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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.github.riteshkukreja.annotations.GeneratedVertical")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MainVerticalGenerator extends AbstractProcessor {

    private final String builderClassName = "MainVerticalGen";
    private final String genPackage = "gen.verticals";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {

            List<String> verticals = roundEnv
                    .getElementsAnnotatedWith(annotation)
                    .stream()
                    .map(a -> genPackage + "." + a.getSimpleName())
                    .collect(Collectors.toList());


            try {
                generateMainVertical(builderClassName, verticals);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private void generateMainVertical(String className, List<String> verticals) throws IOException {
        JavaFileObject builderFile = processingEnv.getFiler()
                .createSourceFile(genPackage + "." + className);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + genPackage + ";");
            out.println("");
            out.println("import io.vertx.core.AbstractVerticle;");

            out.println("");
            out.println("");

            out.println("public class " + className + " extends AbstractVerticle {");

            out.println("");

            out.println(" @Override");
            out.println(" public void start() {");
            verticals.stream()
                    .map(a -> "   vertx.deployVerticle(\"" + a + "\");")
                    .forEach(a -> out.println(a));
            out.println("   }");

            out.println("}");
        }
    }

}
