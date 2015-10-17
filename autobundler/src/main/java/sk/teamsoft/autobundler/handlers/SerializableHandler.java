package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author Dusan Bartos
 */
public class SerializableHandler implements IFieldHandler {
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putSerializable(field.getName(), (Serializable) field.get(object));
        Log.d(getClass().getSimpleName(), "Field saved: " + field.getName());
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getSerializable(field.getName()));
        Log.d(getClass().getSimpleName(), "Field restored: " + field.getName());
    }
}