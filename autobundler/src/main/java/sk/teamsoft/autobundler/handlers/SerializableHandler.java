package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author Dusan Bartos
 */
public final class SerializableHandler implements IFieldHandler {
    private static final String TAG = "SerializableHandler";

    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putSerializable(field.getName(), (Serializable) field.get(object));
        Log.d(TAG, "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getSerializable(field.getName()));
        Log.d(TAG, "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }
}