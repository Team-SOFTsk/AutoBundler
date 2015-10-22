package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;

import java.lang.reflect.Field;

import sk.teamsoft.autobundler.TSLog;

/**
 * Handler for saving/restoring byte value to/from instanceState Bundle
 *
 * @author Dusan Bartos
 */
public class ByteHandler implements IFieldHandler {
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        bundle.putByte(field.getName(), field.getByte(object));
        TSLog.d(object.getClass().getSimpleName(), "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        field.set(object, bundle.getByte(field.getName()));
        TSLog.d(object.getClass().getSimpleName(), "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
    }
}