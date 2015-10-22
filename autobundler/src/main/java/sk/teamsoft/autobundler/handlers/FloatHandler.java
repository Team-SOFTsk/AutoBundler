package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;

import java.lang.reflect.Field;

import sk.teamsoft.autobundler.TSLog;

/**
 * @author Dusan Bartos
 */
public class FloatHandler implements IFieldHandler {
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putFloat(field.getName(), field.getFloat(object));
        TSLog.d(object.getClass().getSimpleName(), "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.setFloat(object, bundle.getFloat(field.getName()));
        TSLog.d(object.getClass().getSimpleName(), "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }
}