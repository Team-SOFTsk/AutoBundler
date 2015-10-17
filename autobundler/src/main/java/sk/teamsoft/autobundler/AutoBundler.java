package sk.teamsoft.autobundler;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;

import sk.teamsoft.autobundler.handlers.BooleanHandler;
import sk.teamsoft.autobundler.handlers.BundleHandler;
import sk.teamsoft.autobundler.handlers.ByteHandler;
import sk.teamsoft.autobundler.handlers.CharHandler;
import sk.teamsoft.autobundler.handlers.DoubleHandler;
import sk.teamsoft.autobundler.handlers.IFieldHandler;
import sk.teamsoft.autobundler.handlers.IntegerHandler;
import sk.teamsoft.autobundler.handlers.LongHandler;
import sk.teamsoft.autobundler.handlers.ParcelableHandler;
import sk.teamsoft.autobundler.handlers.SerializableHandler;
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
                    getTypeHandler(field, component).readValue(field, component, savedInstanceState);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Cannot access field " + field.getName());
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
                    getTypeHandler(field, component).storeValue(field, component, outState);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Cannot access field " + field.getName());
                }
                if (!wasAccessible) {
                    field.setAccessible(false);
                }
            }
        }
    }

    private static IFieldHandler getTypeHandler(final Field iField, final Object iObject)
            throws IllegalAccessException {
        if (String.class.isAssignableFrom(iField.getType())) return new StringHandler();
        if (int.class.isAssignableFrom(iField.getType())) return new IntegerHandler();
        if (Integer.class.isAssignableFrom(iField.getType())) return new IntegerHandler();
        if (double.class.isAssignableFrom(iField.getType())) return new DoubleHandler();
        if (Double.class.isAssignableFrom(iField.getType())) return new DoubleHandler();
        if (boolean.class.isAssignableFrom(iField.getType())) return new BooleanHandler();
        if (Boolean.class.isAssignableFrom(iField.getType())) return new BooleanHandler();
        if (long.class.isAssignableFrom(iField.getType())) return new LongHandler();
        if (Long.class.isAssignableFrom(iField.getType())) return new LongHandler();
        if (char.class.isAssignableFrom(iField.getType())) return new CharHandler();
        if (Character.class.isAssignableFrom(iField.getType())) return new CharHandler();
        if (byte.class.isAssignableFrom(iField.getType())) return new ByteHandler();
        if (Byte.class.isAssignableFrom(iField.getType())) return new ByteHandler();
        if (Bundle.class.isAssignableFrom(iField.getType())) return new BundleHandler();
        if (Serializable.class.isAssignableFrom(iField.getType())) return new SerializableHandler();
        if (Parcelable.class.isAssignableFrom(iField.getType())) return new ParcelableHandler();

        //TODO more handlers (arrays, arraylists, ...)

        if (IFieldHandler.class.isAssignableFrom(iField.getType())) return new IFieldHandler() {
            @Override
            public void storeValue(Field field, Object object, Bundle bundle)
                    throws IllegalAccessException {
                iField.setAccessible(true);
                ((IFieldHandler) iField.get(iObject)).storeValue(field, object, bundle);
                Log.d(iField.getType().getSimpleName(), "Field saved: " + iField.getName());
            }

            @Override
            public void readValue(Field field, Object object, Bundle bundle)
                    throws IllegalAccessException {
                try {
                    iField.setAccessible(true);
                    Object iFieldHandler = iField.getType().newInstance();
                    ((IFieldHandler) iFieldHandler).readValue(field, object, bundle);
                    iField.set(iObject, iFieldHandler);
                    Log.d(iField.getType().getSimpleName(), "Field restored: " + iField.getName());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    Log.e(iField.getType().getSimpleName(), "Cannot instantiate - " + e.getMessage());
                }
            }
        };
//            return (IFieldHandler) iField.get(iObject);

        Log.w(TAG, "Handler for type " + iField.getType().getSimpleName() + " not found");
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
