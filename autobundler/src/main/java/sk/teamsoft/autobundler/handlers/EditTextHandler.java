package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.lang.reflect.Field;

import sk.teamsoft.autobundler.TSLog;

/**
 * Handler for saving/restoring editText state (string value) to/from instanceState Bundle
 *
 * @author Dusan Bartos
 */
public class EditTextHandler implements IFieldHandler {
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        EditText editText = ((EditText) field.get(object));

        if (editText != null) {
            bundle.putString(field.getName(), editText.getText().toString());
            TSLog.d(object.getClass().getSimpleName(), "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
        } else {
            Log.e(object.getClass().getSimpleName(), "Field is null: " + field.getName());
        }
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        EditText editText = ((EditText) field.get(object));

        if (editText != null) {
            editText.setText(bundle.getString(field.getName()));
            TSLog.d(object.getClass().getSimpleName(), "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
        } else {
            Log.e(object.getClass().getSimpleName(), "Field is null: " + field.getName());
        }
    }
}