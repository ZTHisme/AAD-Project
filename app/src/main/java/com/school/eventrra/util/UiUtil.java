package com.school.eventrra.util;

import android.text.Editable;

import com.google.android.material.textfield.TextInputEditText;

public class UiUtil {

    public static String getString(TextInputEditText edt) {
        Editable e = edt.getText();
        return e == null ? null : e.toString();
    }
}
