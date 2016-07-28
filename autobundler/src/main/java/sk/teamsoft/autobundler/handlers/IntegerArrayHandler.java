package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author Dusan Bartos
 */
public final class IntegerArrayHandler implements IFieldHandler {
    private static final String TAG = "IntegerArrayHandler";

    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putIntArray(field.getName(), (int[]) field.get(object));
        Log.d(TAG, "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getIntArray(field.getName()));
        Log.d(TAG, "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }
}