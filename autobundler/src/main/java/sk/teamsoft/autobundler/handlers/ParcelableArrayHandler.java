package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.os.Parcelable;

import java.lang.reflect.Field;

import sk.teamsoft.autobundler.TSLog;

/**
 * @author Dusan Bartos
 */
public class ParcelableArrayHandler implements IFieldHandler {
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putParcelableArray(field.getName(), (Parcelable[]) field.get(object));
        TSLog.d(object.getClass().getSimpleName(), "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getParcelableArray(field.getName()));
        TSLog.d(object.getClass().getSimpleName(), "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }
}
