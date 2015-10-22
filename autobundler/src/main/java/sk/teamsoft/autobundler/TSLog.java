package sk.teamsoft.autobundler;

/**
 * Wrapper class for logging
 * Can be easily switched off for release builds
 *
 * @author Dusan Bartos
 * @see #enableLog(boolean)
 */
@SuppressWarnings("unused")
public class TSLog {
    private static boolean mEnabled = false;
    private static String mLogTag = "TS";

    public static void setTagName(String tagName) {
        mLogTag = tagName;
    }

    public static void enableLog(boolean enable) {
        mEnabled = enable;
    }

    public static void d(String msg) {
        if (mEnabled) android.util.Log.d(mLogTag, msg);
    }

    public static void d(String tag, String msg) {
        if (mEnabled) android.util.Log.d(tag, msg);
    }

    public static void e(String msg) {
        if (mEnabled) android.util.Log.e(mLogTag, msg);
    }

    public static void e(String tag, String msg) {
        if (mEnabled) android.util.Log.e(tag, msg);
    }

    public static void e(String msg, Throwable tr) {
        if (mEnabled) android.util.Log.e(mLogTag, msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (mEnabled) android.util.Log.e(tag, msg, tr);
    }

    public static void i(String msg) {
        if (mEnabled) android.util.Log.i(mLogTag, msg);
    }

    public static void i(String tag, String msg) {
        if (mEnabled) android.util.Log.i(tag, msg);
    }

    public static void v(String msg) {
        if (mEnabled) android.util.Log.v(mLogTag, msg);
    }

    public static void v(String tag, String msg) {
        if (mEnabled) android.util.Log.v(tag, msg);
    }

    public static void w(String msg) {
        if (mEnabled) android.util.Log.w(mLogTag, msg);
    }

    public static void w(String tag, String msg) {
        if (mEnabled) android.util.Log.w(tag, msg);
    }
}
