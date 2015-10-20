package sk.teamsoft.sampleautobundler;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.lang.reflect.Field;

import sk.teamsoft.autobundler.AutoBundlerActivity;
import sk.teamsoft.autobundler.KeepState;
import sk.teamsoft.autobundler.handlers.IFieldHandler;

public class MainActivity extends AutoBundlerActivity {

    @KeepState
    int mId;
    @KeepState
    String mName;
    @KeepState
    double mValue;
    @KeepState
    DataObject mParcel;
    @KeepState
    CustomObject mCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            mId = 12;
            mName = "Test me";
            mValue = 15.5555;
            mParcel = new DataObject(15);
            mCustom = new CustomObject("datadata");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private static class DataObject implements Parcelable {

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
    }

    public static class CustomObject implements IFieldHandler {

        String data;

        public CustomObject() {}

        CustomObject(String data) {
            this.data = data;
        }

        @Override
        public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
            bundle.putString(field.getName(), data);
        }

        @Override
        public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
            data = bundle.getString(field.getName());
        }
    }
}
