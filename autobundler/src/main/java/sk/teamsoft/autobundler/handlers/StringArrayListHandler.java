package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Dusan Bartos
 */
public class StringArrayListHandler implements IFieldHandler {
    @SuppressWarnings("unchecked")
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putStringArrayList(field.getName(), (ArrayList<String>) field.get(object));
        Log.d(getClass().getSimpleName(), "Field saved: " + field.getName());
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getStringArrayList(field.getName()));
        Log.d(getClass().getSimpleName(), "Field restored: " + field.getName());
    }
}