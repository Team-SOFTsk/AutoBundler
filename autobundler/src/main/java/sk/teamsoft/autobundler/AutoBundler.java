package sk.teamsoft.autobundler;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import sk.teamsoft.autobundler.handlers.BooleanHandler;
import sk.teamsoft.autobundler.handlers.BundleHandler;
import sk.teamsoft.autobundler.handlers.ByteHandler;
import sk.teamsoft.autobundler.handlers.CharHandler;
import sk.teamsoft.autobundler.handlers.CharSequenceArrayHandler;
import sk.teamsoft.autobundler.handlers.CharSequenceHandler;
import sk.teamsoft.autobundler.handlers.CheckBoxHandler;
import sk.teamsoft.autobundler.handlers.DoubleHandler;
import sk.teamsoft.autobundler.handlers.EditTextHandler;
import sk.teamsoft.autobundler.handlers.FloatHandler;
import sk.teamsoft.autobundler.handlers.IFieldHandler;
import sk.teamsoft.autobundler.handlers.IntegerArrayHandler;
import sk.teamsoft.autobundler.handlers.IntegerHandler;
import sk.teamsoft.autobundler.handlers.LongHandler;
import sk.teamsoft.autobundler.handlers.ParcelableArrayHandler;
import sk.teamsoft.autobundler.handlers.ParcelableHandler;
import sk.teamsoft.autobundler.handlers.SerializableHandler;
import sk.teamsoft.autobundler.handlers.StringArrayHandler;
import sk.teamsoft.autobundler.handlers.StringHandler;

/**
 * @author Dusan Bartos
 */
public final class AutoBundler {
    private static final String TAG = "AutoBundler";

    private static final Map<Class, StateKeeper> KEEPER_CACHE = new HashMap<>();

    private static AutoBundler sInstance;
    private static IFieldHandler sEmptyHandler;

    static {
        sEmptyHandler = new IFieldHandler() {
            @Override
            public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
            }

            @Override
            public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
            }
        };
    }

    /**
     * @param component          component instance
     * @param savedInstanceState saved state
     * @param mode               restore mode
     */
    static void restore(Object component, Bundle savedInstanceState, @RestoreMode int mode) {
        getInstance().internalRestore(component, savedInstanceState, mode);
    }

    /**
     * @param component component instance
     * @param outState  state
     */
    static void save(Object component, Bundle outState) {
        getInstance().internalStore(component, outState);
    }

    /**
     * Gets singleton instance
     * @return singleton
     */
    private static AutoBundler getInstance() {
        if (sInstance == null) {
            synchronized (AutoBundler.class) {
                if (sInstance == null) {
                    sInstance = new AutoBundler();
                }
            }
        }
        return sInstance;
    }

    private AutoBundler() {}

    private void internalStore(Object component, Bundle outState) {
        try {
            Class<?> keeper = Class.forName(component.getClass().getName() + "$$StateKeeper");
            keeper(keeper).store(component, outState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void internalRestore(Object component, Bundle state, @RestoreMode int mode) {
        try {
            Class<?> keeper = Class.forName(component.getClass().getName() + "$$StateKeeper");
            keeper(keeper).restore(component, state, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StateKeeper keeper(Class<?> clz) throws IllegalAccessException, InstantiationException {
        if (KEEPER_CACHE.containsKey(clz)) {
            return KEEPER_CACHE.get(clz);
        }
        StateKeeper o = (StateKeeper) clz.newInstance();
        KEEPER_CACHE.put(clz, o);
        return o;
    }

    /**
     * @param iField  field
     * @param iObject component instance
     * @return bundle handler for exact type
     *
     * @throws IllegalAccessException
     */
    private IFieldHandler getTypeHandler(final Field iField, final Object iObject)
            throws IllegalAccessException {
        if (String.class.isAssignableFrom(iField.getType())) return new StringHandler();
        if (String[].class.isAssignableFrom(iField.getType())) return new StringArrayHandler();
        if (CharSequence.class.isAssignableFrom(iField.getType())) return new CharSequenceHandler();
        if (CharSequence[].class.isAssignableFrom(iField.getType()))
            return new CharSequenceArrayHandler();
        if (int.class.isAssignableFrom(iField.getType())) return new IntegerHandler();
        if (Integer.class.isAssignableFrom(iField.getType())) return new IntegerHandler();
        if (int[].class.isAssignableFrom(iField.getType())) return new IntegerArrayHandler();
        if (Integer[].class.isAssignableFrom(iField.getType())) return new IntegerArrayHandler();
        if (double.class.isAssignableFrom(iField.getType())) return new DoubleHandler();
        if (Double.class.isAssignableFrom(iField.getType())) return new DoubleHandler();
        if (float.class.isAssignableFrom(iField.getType())) return new FloatHandler();
        if (Float.class.isAssignableFrom(iField.getType())) return new FloatHandler();
        if (boolean.class.isAssignableFrom(iField.getType())) return new BooleanHandler();
        if (Boolean.class.isAssignableFrom(iField.getType())) return new BooleanHandler();
        if (long.class.isAssignableFrom(iField.getType())) return new LongHandler();
        if (Long.class.isAssignableFrom(iField.getType())) return new LongHandler();
        if (char.class.isAssignableFrom(iField.getType())) return new CharHandler();
        if (Character.class.isAssignableFrom(iField.getType())) return new CharHandler();
        if (byte.class.isAssignableFrom(iField.getType())) return new ByteHandler();
        if (Byte.class.isAssignableFrom(iField.getType())) return new ByteHandler();
        if (Bundle.class.isAssignableFrom(iField.getType())) return new BundleHandler();
        if (Parcelable.class.isAssignableFrom(iField.getType())) return new ParcelableHandler();
        if (Parcelable[].class.isAssignableFrom(iField.getType()))
            return new ParcelableArrayHandler();

        // handles also arrayLists since arrayList implements Serializable
        if (Serializable.class.isAssignableFrom(iField.getType())) return new SerializableHandler();

        //TODO more handlers

        if (IFieldHandler.class.isAssignableFrom(iField.getType())) return new IFieldHandler() {
            @Override
            public void storeValue(Field field, Object object, Bundle bundle)
                    throws IllegalAccessException {
                boolean wasAccessible = iField.isAccessible();
                iField.setAccessible(true);
                ((IFieldHandler) iField.get(iObject)).storeValue(field, object, bundle);
                Log.d(iObject.getClass().getSimpleName(), "Field saved: " + iField.getName());
                if (!wasAccessible) {
                    iField.setAccessible(false);
                }
            }

            @Override
            public void readValue(Field field, Object object, Bundle bundle)
                    throws IllegalAccessException {
                boolean wasAccessible = iField.isAccessible();
                iField.setAccessible(true);
                try {
                    Object iFieldHandler = iField.getType().newInstance();
                    ((IFieldHandler) iFieldHandler).readValue(field, object, bundle);
                    iField.set(iObject, iFieldHandler);
                    Log.d(iObject.getClass().getSimpleName(), "Field restored: " + iField.getName());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    Log.e(iObject.getClass().getSimpleName(), "Cannot instantiate - " + e.getMessage());
                }

                if (!wasAccessible) {
                    iField.setAccessible(false);
                }
            }
        };

        // handles EditText and AppcompatEditText
        if (EditText.class.isAssignableFrom(iField.getType())) return new EditTextHandler();
        if (CheckBox.class.isAssignableFrom(iField.getType())) return new CheckBoxHandler();

        Log.w(TAG, "Handler for type " + iField.getType().getSimpleName() + " not found");
        return sEmptyHandler;
    }
}
