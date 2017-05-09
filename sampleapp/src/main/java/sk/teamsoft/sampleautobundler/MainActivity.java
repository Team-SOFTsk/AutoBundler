package sk.teamsoft.sampleautobundler;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import sk.teamsoft.autobundler.AutoBundler;
import sk.teamsoft.autobundler.KeepState;

import static sk.teamsoft.autobundler.AutoBundlerConfig.MODE_ONRESTORE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @KeepState int mId;
    @KeepState String mName;
    @KeepState(mode = MODE_ONRESTORE) double mValue;
    @KeepState(mode = MODE_ONRESTORE) DataObject mParcel;
    @KeepState(mode = MODE_ONRESTORE) ArrayList<String> mArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoBundler.onCreate(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Log.d(TAG, "init");

            mId = 12;
            mName = "Test me";
            mValue = 15.5555;
            mParcel = new DataObject(15);
            mArray = new ArrayList<>();
            mArray.add("First");
            mArray.add("Second");
        }

        Log.d(TAG, "onCreate");
        Log.v(TAG, "id=" + mId);
        Log.v(TAG, "name=" + mName);
        Log.v(TAG, "value=" + mValue);
        Log.v(TAG, "parcel=" + mParcel);
        Log.v(TAG, "array=" + mArray);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        AutoBundler.onRestore(this, savedInstanceState);

        Log.d(TAG, "onRestoreInstanceState");
        Log.v(TAG, "id=" + mId);
        Log.v(TAG, "name=" + mName);
        Log.v(TAG, "value=" + mValue);
        Log.v(TAG, "parcel=" + mParcel);
        Log.v(TAG, "array=" + mArray);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        AutoBundler.onSave(this, outState);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState " + outState);
    }

    public static class DataObject implements Parcelable {
        int data;

        public static final Creator<DataObject> CREATOR = new Creator<DataObject>() {
            @Override
            public DataObject createFromParcel(Parcel in) {
                return new DataObject(in);
            }

            @Override
            public DataObject[] newArray(int size) {
                return new DataObject[size];
            }
        };

        DataObject(int data) {
            this.data = data;
        }

        DataObject(Parcel parcel) {
            data = parcel.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(data);
        }

        @Override public String toString() {
            return "DataObject{" +
                    "data=" + data +
                    '}';
        }
    }
}
