package sk.teamsoft.autobundler;

import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dusan Bartos
 */
public final class AutoBundler {
    private static final Map<Class, StateKeeper> KEEPER_CACHE = new HashMap<>();
    private static AutoBundler sInstance;

    /**
     * @param component          component instance
     * @param savedInstanceState saved state
     */
    public static void onRestore(Object component, Bundle savedInstanceState) {
        getInstance().internalRestore(component, savedInstanceState, AutoBundlerConfig.MODE_ONRESTORE);
    }

    /**
     * @param component          component instance
     * @param savedInstanceState saved state
     */
    public static void onCreate(Object component, Bundle savedInstanceState) {
        getInstance().internalRestore(component, savedInstanceState, AutoBundlerConfig.MODE_ONCREATE);
    }

    /**
     * @param component component instance
     * @param outState  state
     */
    public static void onSave(Object component, Bundle outState) {
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
        if (outState == null) return;

        try {
            Class<?> keeper = getComponentKeeper(component);
            Log.d(keeper.getSimpleName(), "store: " + component.getClass().getSimpleName());
            //noinspection unchecked
            getKeeper(keeper).store(component, outState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void internalRestore(Object component, Bundle state, @RestoreMode int mode) {
        if (state == null) return;

        try {
            Class<?> keeper = getComponentKeeper(component);
            Log.v(keeper.getSimpleName(), "restore: " + component.getClass().getSimpleName() + " [mode=" + mode + "]");
            //noinspection unchecked
            getKeeper(keeper).restore(component, state, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Class<?> getComponentKeeper(Object component) throws ClassNotFoundException {
        return Class.forName(component.getClass().getName() + "$$StateKeeper");
    }

    private StateKeeper getKeeper(Class<?> clz) throws IllegalAccessException, InstantiationException {
        if (KEEPER_CACHE.containsKey(clz)) {
            return KEEPER_CACHE.get(clz);
        }
        StateKeeper o = (StateKeeper) clz.newInstance();
        KEEPER_CACHE.put(clz, o);
        return o;
    }
}
