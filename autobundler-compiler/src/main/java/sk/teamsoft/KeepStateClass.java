package sk.teamsoft;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * @author Dusan Bartos
 */
final class KeepStateClass {

    private static final ClassName STATE_KEEPER = ClassName.get("sk.teamsoft.autobundler.internal", "StateKeeper");
    private static final ClassName CONTEXT = ClassName.get("android.content", "Context");

    private final boolean isFinal;
    private final TypeName targetTypeName;
    private final ClassName keeperClassName;

    KeepStateClass(TypeName targetTypeName, ClassName keeperClassName, boolean isFinal) {
        this.isFinal = isFinal;
        this.targetTypeName = targetTypeName;
        this.keeperClassName = keeperClassName;
    }

    Collection<JavaFile> brewJava() {
        TypeSpec.Builder result = TypeSpec.classBuilder(keeperClassName)
                .addModifiers(PUBLIC, FINAL)
                .addSuperinterface(ParameterizedTypeName.get(STATE_KEEPER, targetTypeName));

        result.addMethod(createKeepMethod(targetTypeName));

        List<JavaFile> files = new ArrayList<>();
        /*if (!isFinal) {
            result.addMethod(createBindToTargetMethod());
        }*/

        files.add(JavaFile.builder(keeperClassName.packageName(), result.build())
                .addFileComment("Generated code from AutoBundler. Do not modify!")
                .build());

        return files;
    }

    private MethodSpec createKeepMethod(TypeName targetType) {
        MethodSpec.Builder result = MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .returns(Void.TYPE);
//                .addParameter(FINDER, "finder");
        result.addParameter(targetType, "target");
        result.addParameter(Object.class, "source");

//        result.addStatement("$T context = finder.getContext(source)", CONTEXT);

        if (isFinal) {
            generateBindViewBody(result);
            result.addCode("\n");
        }

        //TODO
        CodeBlock.Builder invoke = CodeBlock.builder();
        if (!isFinal) {
//            invoke.add("$N", BIND_TO_TARGET);
        }
        if (!isFinal) {
            invoke.add("(target");
//            if (needsFinder) invoke.add(", finder, source");
            result.addStatement("$L", invoke.add(")").build());
        }

        return result.build();
    }

    private void generateBindViewBody(MethodSpec.Builder result) {
        //TODO
    }
}
