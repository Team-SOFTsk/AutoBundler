package sk.teamsoft.autobundler;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Dusan Bartos
 */
public abstract class AutoBundlerDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            AutoBundler.restore(this, savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        AutoBundler.save(this, outState);
        super.onSaveInstanceState(outState);
    }

    /*@Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        AutoBundler.restore(this, savedInstanceState);
    }*/
}
