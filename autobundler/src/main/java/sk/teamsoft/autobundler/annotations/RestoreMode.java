package sk.teamsoft.autobundler.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import sk.teamsoft.autobundler.AutoBundler;

/**
 * @author Dusan Bartos
 */
@android.support.annotation.IntDef({AutoBundler.MODE_ONCREATE, AutoBundler.MODE_ONRESTORE})
@Retention(RetentionPolicy.SOURCE)
public @interface RestoreMode {}
