package sk.teamsoft.autobundler;

import android.os.Bundle;

/**
 * @author Dusan Bartos
 */
public abstract class AutoBundlerFragment extends android.support.v4.app.Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            AutoBundler.restore(this, savedInstanceState, AutoBundler.MODE_ONCREATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        AutoBundler.save(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        AutoBundler.restore(this, savedInstanceState, AutoBundler.MODE_ONRESTORE);
    }
}
