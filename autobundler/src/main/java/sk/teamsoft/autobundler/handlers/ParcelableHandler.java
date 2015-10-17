package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author Dusan Bartos
 */
public class ParcelableHandler implements IFieldHandler {
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putParcelable(field.getName(), (Parcelable) field.get(object));
        Log.d(getClass().getSimpleName(), "Field saved: " + field.getName());
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getParcelable(field.getName()));
        Log.d(getClass().getSimpleName(), "Field restored: " + field.getName());
    }
}
