package sk.teamsoft;

import android.os.Parcelable;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import sk.teamsoft.autobundler.KeepState;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AutoBundlerProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;
    private boolean generatedElements;

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
        typeUtils = env.getTypeUtils();
        elementUtils = env.getElementUtils();
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        final Set<String> types = new LinkedHashSet<>();
        types.add(KeepState.class.getCanonicalName());
        //        types.add(RestoreMode.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        processStatefulElements(env);
        return false;
    }

    private void processStatefulElements(RoundEnvironment env) {
        if (generatedElements) {
            //            System.out.println("elements already processed");
            return;
        }

        System.out.println("processing stateful elements");
        generatedElements = true;

        try {
            //map of class containers [key] and processor elements [values]
            final Map<Element, List<Element>> containerMap = new HashMap<>();

            env.getElementsAnnotatedWith(KeepState.class).forEach(element -> {
                List<Element> containerElems = containerMap.get(element.getEnclosingElement());
                if (containerElems == null) {
                    containerElems = new ArrayList<>();
                    containerMap.put(element.getEnclosingElement(), containerElems);
                }
                containerElems.add(element);
            });

            containerMap.forEach((container, elements) -> {
                final String packageName = container.getEnclosingElement().toString();
                final String fileName = container.getSimpleName().toString();

                System.out.println("process " + packageName + "." + fileName);

                ParameterizedTypeName keeperType = ParameterizedTypeName.get(
                        ClassName.get("sk.teamsoft.autobundler", "StateKeeper"),
                        TypeName.get(container.asType()));

                final AnnotationSpec.Builder suppress = AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "\"unchecked\"");

                final TypeSpec.Builder result = TypeSpec.classBuilder(fileName + "$$StateKeeper")
                        .addSuperinterface(keeperType)
                        .addAnnotation(suppress.build())
                        .addModifiers(PUBLIC, FINAL);

                final MethodSpec.Builder storeMethod = MethodSpec.methodBuilder("store");
                final MethodSpec.Builder restoreMethod = MethodSpec.methodBuilder("restore");
                final CodeBlock.Builder storeBlock = CodeBlock.builder();
                final CodeBlock.Builder restoreBlock = CodeBlock.builder();
                storeMethod.addModifiers(PUBLIC);
                restoreMethod.addModifiers(PUBLIC);
                storeMethod.addAnnotation(Override.class);
                restoreMethod.addAnnotation(Override.class);
                storeMethod.addParameter(TypeName.get(container.asType()), "target");
                storeMethod.addParameter(ClassName.get("android.os", "Bundle"), "bundle");
                restoreMethod.addParameter(TypeName.get(container.asType()), "target");
                restoreMethod.addParameter(ClassName.get("android.os", "Bundle"), "bundle");
                restoreMethod.addParameter(Integer.class, "mode");

                //storeBlock.add("super.store();");
                //restoreBlock.add("super.restore();");

                elements.forEach(element -> {
                    final KeepState annotation = element.getAnnotation(KeepState.class);

                    storeBlock.add(resolveStoreCode(element) + "\n");
                    /*restoreBlock.add("target." + element + " = (" + element.asType().toString() + ") " +
                            "bundle.get(\"" + element.getSimpleName() + "\");  //mode=" + annotation.mode() + "\n");*/
                    restoreBlock.add("if (mode==" + annotation.mode() + ")" +
                            " target." + element + " = (" + element.asType().toString() + ") " +
                            "bundle.get(\"" + element.getSimpleName() + "\");  //mode=" + annotation.mode() + "\n");
                });

                storeMethod.addCode(storeBlock.build());
                restoreMethod.addCode(restoreBlock.build());

                result.addMethod(storeMethod.build());
                result.addMethod(restoreMethod.build());

                try {
                    final JavaFile javaFile = JavaFile.builder(packageName, result.build())
                            .addFileComment("Generated Autobundler code. Do not modify!")
                            .build();

                    System.out.println("write file " + fileName + "$$StateKeeper");
                    javaFile.writeTo(filer);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Cannot process " + container.toString() + ": " + e.getClass().getSimpleName() + "[" + e.getMessage() + "]");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot process elements: " + e.getClass().getSimpleName() + "[" + e.getMessage() + "]");
        }
    }

    private String resolveStoreCode(Element element) {
        return "bundle." + resolveStoreAction(element) + "(\"" + element.getSimpleName() + "\", target." + element + ");";
    }

    private String resolveStoreAction(Element element) {
        if (isAssignable(element, CharSequence.class)) {
            return "putCharSequence";
        } else if (isAssignable(element, CharSequence[].class)) {
            return "putCharSequenceArray";
        } else if (isAssignable(element, Integer.class)) {
            return "putInt";
        } else if (isAssignable(element, Integer[].class)) {
            return "putIntArray";
        } else if (isAssignable(element, Double.class)) {
            return "putDouble";
        } else if (isAssignable(element, Float.class)) {
            return "putFloat";
        } else if (isAssignable(element, Boolean.class)) {
            return "putBoolean";
        } else if (isAssignable(element, Long.class)) {
            return "putLong";
        } else if (isAssignable(element, Byte.class)) {
            return "putByte";
        } else if (isAssignable(element, Parcelable.class)) {
            return "putParcelable";
        } else if (isAssignable(element, Parcelable[].class)) {
            return "putParcelableArray";
        } else if (isAssignable(element, Serializable.class)) {
            return "putSerializable";
        }

        throw new IllegalStateException("No bundle method found for type " + element.asType());
    }

    private boolean isAssignable(Element element, Class clz) {
        //        System.out.println("isAssignable elem=" + element + " cls=" + clz.getCanonicalName());
        final TypeMirror secondType;
        if (clz.isArray()) {
            secondType = typeUtils.getArrayType(elementUtils.getTypeElement(clz.getComponentType().getCanonicalName()).asType());
        } else {
            TypeElement typeElement = elementUtils.getTypeElement(clz.getCanonicalName());
            secondType = typeElement.asType();
        }
        //        System.out.println("isAssignable type=" + secondType);

        final TypeMirror a = element.asType();
        //        System.out.println("isAssignable a=" + a + " b=" + secondType);
        return typeUtils.isAssignable(a, secondType);
    }
}
