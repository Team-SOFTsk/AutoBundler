package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author Dusan Bartos
 */
public class IntegerHandler implements IFieldHandler {
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putInt(field.getName(), field.getInt(object));
        Log.d(getClass().getSimpleName(), "Field saved: " + field.getName());
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getInt(field.getName()));
        Log.d(getClass().getSimpleName(), "Field restored: " + field.getName());
    }
}