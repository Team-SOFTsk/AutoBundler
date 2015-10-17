package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;

import java.lang.reflect.Field;

/**
 * @author Dusan Bartos
 */
public interface IFieldHandler {
    void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException;

    void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException;
}
