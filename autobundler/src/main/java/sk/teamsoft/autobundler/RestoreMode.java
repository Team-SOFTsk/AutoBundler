package sk.teamsoft.autobundler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Dusan Bartos
 */
@android.support.annotation.IntDef({AutoBundler.MODE_ONCREATE, AutoBundler.MODE_ONRESTORE})
@Retention(RetentionPolicy.SOURCE)
public @interface RestoreMode {}
