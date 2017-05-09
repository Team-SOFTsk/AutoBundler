package sk.teamsoft.sampleautobundler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import sk.teamsoft.autobundler.AutoBundler;

/**
 * @author Dusan Bartos
 *         Created on 09.05.2017.
 */

public class NonKeeperActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoBundler.onCreate(this, savedInstanceState);
        setContentView(R.layout.activity_nonkeeper);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        AutoBundler.onRestore(this, savedInstanceState);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        AutoBundler.onSave(this, outState);
        super.onSaveInstanceState(outState);
    }
}
