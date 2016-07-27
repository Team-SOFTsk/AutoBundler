package sk.teamsoft.autobundler;

import android.os.Bundle;

/**
 * @author Dusan Bartos
 */
public abstract class AutoBundlerActivity extends android.support.v7.app.AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            AutoBundler.restore(this, savedInstanceState, AutoBundlerConfig.MODE_ONCREATE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        AutoBundler.save(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        AutoBundler.restore(this, savedInstanceState, AutoBundlerConfig.MODE_ONRESTORE);
    }
}
