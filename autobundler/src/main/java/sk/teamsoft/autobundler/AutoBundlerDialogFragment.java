package sk.teamsoft.autobundler;

import android.os.Bundle;
import android.view.View;

/**
 * @author Dusan Bartos
 */
public abstract class AutoBundlerDialogFragment extends android.support.v4.app.DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            AutoBundler.restore(this, savedInstanceState, AutoBundlerConfig.MODE_ONCREATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        AutoBundler.save(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AutoBundler.restore(this, savedInstanceState, AutoBundlerConfig.MODE_ONRESTORE);
    }
}
