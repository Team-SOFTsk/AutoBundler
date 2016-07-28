package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;

import java.lang.reflect.Field;

/**
 * Implementation used to store and restore data from an object to bundle
 * Can be used directly with own class type
 *
 * @author Dusan Bartos
 */
public interface IFieldHandler {
    void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException;

    void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException;
}
