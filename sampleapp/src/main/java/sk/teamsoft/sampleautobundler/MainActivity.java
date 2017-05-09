package sk.teamsoft.sampleautobundler;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import sk.teamsoft.autobundler.AutoBundlerActivity;
import sk.teamsoft.autobundler.KeepState;

import static sk.teamsoft.autobundler.AutoBundlerConfig.MODE_ONRESTORE;

public class MainActivity extends AutoBundlerActivity {

    @KeepState int mId;
    @KeepState String mName;
    @KeepState(mode = MODE_ONRESTORE) double mValue;
    @KeepState(mode = MODE_ONRESTORE) DataObject mParcel;
    @KeepState ArrayList<String> mArray;
//    @KeepState(mode = AutoBundlerConfig.MODE_ONRESTORE) EditText mEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mEdittext = (EditText) findViewById(R.id.edittext);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            mId = 12;
            mName = "Test me";
            mValue = 15.5555;
            mParcel = new DataObject(15);
            mArray = new ArrayList<>();
            mArray.add("First");
            mArray.add("Second");
        }
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
    }
}
