package com.example.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({"com.example.processor.TestClass","com.example.processor.TestMethod","com.example.processor.TestLogic"})
public class TestAnnotationProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(TestClass.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "@TestClass can only be applied to classes", element);
                continue;
            }

            TypeElement classElement = (TypeElement) element;
            generateTestClass(classElement);
        }

        return true;
    }

    private void generateTestClass(TypeElement classElement) {
        String testClassName = classElement.getSimpleName() + "Test";
        String packageName = processingEnv.getElementUtils().getPackageOf(classElement).toString();
        try {
            Filer filer = processingEnv.getFiler();
            FileObject fileObject = filer.createResource(StandardLocation.SOURCE_OUTPUT, "test", "com/example/" + testClassName + ".java");
            PrintWriter writer = new PrintWriter(fileObject.openWriter());

            writer.println("import " + packageName + ".*;");
            writer.println("import org.junit.Test;\n");
            writer.println();
            writer.println("public class " + testClassName + " {");


            List<ExecutableElement> methods = ElementFilter.methodsIn(classElement.getEnclosedElements());
            for (ExecutableElement methodElement : methods) {
                if (methodElement.getAnnotation(TestMethod.class) != null) {
                    String methodName = methodElement.getSimpleName().toString();
                    writer.println("  @Test");
                    writer.println("  public void " + methodName + "() {");

                    // Generate test logic from @TestLogic methods
                    for (ExecutableElement logicElement : methods) {
                        TestLogic testLogicAnnotation = logicElement.getAnnotation(TestLogic.class);
                        if (testLogicAnnotation != null && testLogicAnnotation.forMethod().equals(methodName)) {
                            String logicMethodName = logicElement.getSimpleName().toString();
                            writer.println("    " + classElement.getSimpleName() + " mainInstance = new " + classElement.getSimpleName() + "();");
                            writer.println("    " + "mainInstance."+logicMethodName + "();");
                        }
                    }

                    writer.println("  }");
                }
            }

            writer.println("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
