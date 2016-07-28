package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author Dusan Bartos
 */
public final class ParcelableHandler implements IFieldHandler {
    private static final String TAG = "ParcelableHandler";

    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putParcelable(field.getName(), (Parcelable) field.get(object));
        Log.d(TAG, "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getParcelable(field.getName()));
        Log.d(TAG, "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }
}
