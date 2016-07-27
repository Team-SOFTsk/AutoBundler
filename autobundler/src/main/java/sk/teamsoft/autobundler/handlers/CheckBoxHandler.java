package sk.teamsoft.autobundler.handlers;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import java.lang.reflect.Field;

/**
 * Handler for saving/restoring checkbox state to/from instanceState Bundle
 *
 * @author Dusan Bartos
 */
public final class CheckBoxHandler implements IFieldHandler {
    @Override
    public void storeValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        CheckBox checkBox = ((CheckBox) field.get(object));

        if (checkBox != null) {
            bundle.putBoolean(field.getName(), checkBox.isChecked());
            Log.d(object.getClass().getSimpleName(), "Field saved: " + field.getName() + " (" + getClass().getSimpleName() + ")");
        } else {
            Log.e(object.getClass().getSimpleName(), "Field is null: " + field.getName());
        }
    }

    @Override
    public void readValue(Field field, Object object, Bundle bundle) throws IllegalAccessException {
        CheckBox checkBox = ((CheckBox) field.get(object));

        if (checkBox != null) {
            checkBox.setChecked(bundle.getBoolean(field.getName()));
            Log.d(object.getClass().getSimpleName(), "Field restored: " + field.getName() + " (" + getClass().getSimpleName() + ")");
        } else {
            Log.e(object.getClass().getSimpleName(), "Field is null: " + field.getName());
        }
    }
}