package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author Dusan Bartos
 */
public final class FloatHandler implements IFieldHandler {
    private static final String TAG = "FloatHandler";

    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putFloat(field.getName(), field.getFloat(object));
        Log.d(TAG, "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.setFloat(object, bundle.getFloat(field.getName()));
        Log.d(TAG, "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }
}