package sk.teamsoft.autobundler.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sk.teamsoft.autobundler.AutoBundler;

/**
 * @author Dusan Bartos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KeepState {
    @RestoreMode int mode() default AutoBundler.MODE_ONCREATE;
}
