package sk.teamsoft.autobundler;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dusan Bartos
 */
public final class AutoBundler {
    private static final Map<Class, StateKeeper> KEEPER_CACHE = new HashMap<>();
    private static final Set<String> NONKEEPER_CACHE = new HashSet<>();
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

    private void internalStore(final Object component, final Bundle outState) {
        if (outState == null) return;

        getKeeperAndCall(component, new KeeperCallback() {
            @Override public void call(StateKeeper keeper) {
                Log.v(keeper.getClass().getSimpleName(), "store: " + component.getClass().getSimpleName());
                //noinspection unchecked
                keeper.store(component, outState);
            }
        });
    }

    private void internalRestore(final Object component, final Bundle state, final @RestoreMode int mode) {
        if (state == null) return;

        getKeeperAndCall(component, new KeeperCallback() {
            @Override public void call(StateKeeper keeper) {
                Log.v(keeper.getClass().getSimpleName(), "restore: " + component.getClass().getSimpleName() + " [mode=" + mode + "]");
                //noinspection unchecked
                keeper.restore(component, state, mode);
            }
        });
    }

    private void getKeeperAndCall(Object component, KeeperCallback callable) {
        if (NONKEEPER_CACHE.contains(component.getClass().getName())) return;

        try {
            Class<?> keeper = getComponentKeeper(component);
            callable.call(getKeeper(keeper));
        } catch (Exception e) {
            if (e instanceof ClassNotFoundException) {
                for (Field field : component.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(KeepState.class)) {
                        e.printStackTrace();
                        throw new IllegalStateException("AutoBundler: StateKeeper error: " + e.getMessage());
                    }
                }
                Log.v("AutoBundler", "component " + component.getClass().getSimpleName() +
                        " does not have a StateKeeper. It will be cached as non-keeper class");
                NONKEEPER_CACHE.add(component.getClass().getName());
            } else {
                e.printStackTrace();
                throw new IllegalStateException("AutoBundler: StateKeeper error: " + e.getMessage());
            }
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

    interface KeeperCallback {
        void call(StateKeeper keeper);
    }
}
