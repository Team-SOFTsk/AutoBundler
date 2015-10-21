# AutoBundler
Android AutoBundler library

[ ![Download](https://api.bintray.com/packages/team-softsk/maven/autobundler/images/download.svg) ](https://bintray.com/team-softsk/maven/autobundler/_latestVersion)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/sk.teamsoft/autobundler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/sk.teamsoft/autobundler)

### Description
AutoBundler is a library for Android applications, which removes boilerplate code from your
activities and fragments. The library handles typical actions with saving instance state.
So overriding `onSaveInstanceState` and `onRestoreInstanceState` is a history now.

### Usage
All you need to do to make AutoBundler work, is to inherit from `AutoBundlerActivity`, 
`AutoBundlerFragment` or `AutoBundlerDialogFragment` classes and add annotations to specified fields.
That's all.

    public class MainActivity extends AutoBundlerActivity {
        @KeepState
        int mId;
        @KeepState
        String mName;
        @KeepState
        double mValue;
    }
    
    
AutoBundler supports basic classes, but you can also define your own class, which can either
implement `Parcelable` or `IFieldHandler` interface. You can then specify the exact algorithm
used to save and restore object values.
 
    @KeepState
    DataObject mParcel;
    @KeepState
    CustomObject mCustom;
    
    ...
    
    class DataObject implements Parcelable {...}
    
    class CustomObject implements IFieldHandler {
            ...
            
            @Override
            public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
                bundle.putString(field.getName(), data);
            }
    
            @Override
            public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
                data = bundle.getString(field.getName());
            }
        }

### License
Released under Apache v2 license

### Contact
[Team-SOFT s.r.o.](https://teamsoft.sk)