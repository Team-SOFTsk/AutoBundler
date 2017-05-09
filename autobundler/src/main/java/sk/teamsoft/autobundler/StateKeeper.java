package sk.teamsoft.autobundler;

import android.os.Bundle;

/**
 * @author Dusan Bartos
 *         Created on 05.05.2017.
 */

public interface StateKeeper<T> {
    void store(T target, Bundle bundle);

    void restore(T target, Bundle bundle, Integer mode);
}
