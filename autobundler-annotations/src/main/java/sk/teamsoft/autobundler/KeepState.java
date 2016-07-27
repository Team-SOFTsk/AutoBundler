package sk.teamsoft.autobundler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dusan Bartos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KeepState {
    @RestoreMode int mode() default AutoBundlerConfig.MODE_ONCREATE;
}
