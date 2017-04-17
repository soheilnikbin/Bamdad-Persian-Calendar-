package com.outlay.view.numpad;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.byagowi.persiancalendar.view.activity.ConvertPersianCal;
import com.omninotes.BaseActivity;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class SimpleNumpadValidator implements NumpadValidator {
    @Override
    public boolean valid(String pvalue) {
        String value = ConvertPersianCal.toEnglishNumber(pvalue);
        if (TextUtils.isEmpty(value) || value.length() > 13) {
            return false;
        }
        try {
            Double.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onInvalidInput(String value) {

    }
}
