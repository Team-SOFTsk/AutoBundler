package sk.teamsoft.sampleautobundler;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.ArrayList;

import sk.teamsoft.autobundler.AutoBundler;
import sk.teamsoft.autobundler.annotations.KeepState;
import sk.teamsoft.autobundler.handlers.IFieldHandler;

public class MainActivity extends AppCompatActivity {

    @KeepState int id;
    @KeepState String name;
    @KeepState double value;
    @KeepState DataObject dataObject;
    @KeepState CustomObject customObject;
    @KeepState ArrayList<String> strings;
    @KeepState(mode = AutoBundler.MODE_ONRESTORE) EditText editText;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = (EditText) findViewById(R.id.edittext);

        AutoBundler.restore(this, savedInstanceState, AutoBundler.MODE_ONCREATE);

        if (savedInstanceState == null) {
            id = 12;
            name = "Test me";
            value = 15.5555;
            dataObject = new DataObject(15);
            customObject = new CustomObject("datadata");
            strings = new ArrayList<>();
            strings.add("First");
            strings.add("Second");
        }
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        AutoBundler.restore(this, savedInstanceState, AutoBundler.MODE_ONRESTORE);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        AutoBundler.save(this, outState);
        super.onSaveInstanceState(outState);
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

        //need this because of automatic instantiation within autobundler
        public CustomObject() {
        }

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
