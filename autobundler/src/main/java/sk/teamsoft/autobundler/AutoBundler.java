package sk.teamsoft.autobundler;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Field;

import sk.teamsoft.autobundler.handlers.DoubleHandler;
import sk.teamsoft.autobundler.handlers.IFieldHandler;
import sk.teamsoft.autobundler.handlers.IntegerHandler;
import sk.teamsoft.autobundler.handlers.ParcelableHandler;
import sk.teamsoft.autobundler.handlers.StringHandler;

/**
 * @author Dusan Bartos
 */
public class AutoBundler {
    private static final String TAG = AutoBundler.class.getSimpleName();

    protected static void restore(Object component, Bundle savedInstanceState) {
        for (Field field : component.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(KeepState.class)) {
                field.setAccessible(true);
                try {
                    getTypeHandler(field.getType()).readValue(field, component, savedInstanceState);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.w(TAG, "Cannot access field " + field.getName());
                }
            }
        }
    }

    protected static void save(Object component, Bundle outState) {
        for (Field field : component.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(KeepState.class)) {
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    getTypeHandler(field.getType()).storeValue(field, component, outState);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.w(TAG, "Cannot access field " + field.getName());
                }
                if (!wasAccessible) {
                    field.setAccessible(false);
                }
            }
        }
    }

    private static IFieldHandler getTypeHandler(Class<?> type) {
        if (String.class.isAssignableFrom(type)) return new StringHandler();
        if (int.class.isAssignableFrom(type)) return new IntegerHandler();
        if (Integer.class.isAssignableFrom(type)) return new IntegerHandler();
        if (double.class.isAssignableFrom(type)) return new DoubleHandler();
        if (Double.class.isAssignableFrom(type)) return new DoubleHandler();
        if (Parcelable.class.isAssignableFrom(type)) return new ParcelableHandler();

        //TODO more handlers

        Log.w(TAG, "Handler for type " + type.getSimpleName() + " not found");
        return new IFieldHandler() {
            @Override
            public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
            }

            @Override
            public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
            }
        };
    }
}
