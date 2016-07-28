package sk.teamsoft;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import sk.teamsoft.autobundler.KeepState;

import static javax.tools.Diagnostic.Kind.ERROR;

@AutoService(Processor.class)
public final class AutoBundlerProcessor extends AbstractProcessor {

//    private Elements elementUtils;
//    private Types typeUtils;
    private Filer filer;

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(processingEnv);

//        elementUtils = env.getElementUtils();
//        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Map<TypeElement, KeepStateClass> targetClassMap = findAndParseTargets(env);

        for (Map.Entry<TypeElement, KeepStateClass> entry : targetClassMap.entrySet()) {
            TypeElement typeElement = entry.getKey();
            KeepStateClass keepStateClass = entry.getValue();

            for (JavaFile javaFile : keepStateClass.brewJava()) {
                try {
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    error(typeElement, "Unable to write state keeper for type %s: %s", typeElement,
                            e.getMessage());
                }
            }
        }

        return true;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

        annotations.add(KeepState.class);

        return annotations;
    }

    //TODO
    private Map<TypeElement, KeepStateClass> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, KeepStateClass> targetClassMap = new LinkedHashMap<>();
        Set<TypeElement> erasedTargetNames = new LinkedHashSet<>();

        // Process each @KeepState element.
        for (Element element : env.getElementsAnnotatedWith(KeepState.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                //TODO process
            } catch (Exception e) {
                logParsingError(element, KeepState.class, e);
            }
        }

        return targetClassMap;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(ERROR, message, element);
    }

    private void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s binding.\n\n%s", annotation.getSimpleName(), stackTrace);
    }
}
