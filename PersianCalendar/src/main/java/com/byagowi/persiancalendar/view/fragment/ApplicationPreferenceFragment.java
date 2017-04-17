package com.byagowi.persiancalendar.view.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.util.Utils;
import com.byagowi.persiancalendar.view.preferences.GPSLocationDialog;
import com.byagowi.persiancalendar.view.preferences.GPSLocationPreference;
import com.byagowi.persiancalendar.view.preferences.LocationPreference;
import com.byagowi.persiancalendar.view.preferences.PrayerSelectDialog;
import com.byagowi.persiancalendar.view.preferences.PrayerSelectPreference;
import com.byagowi.persiancalendar.view.preferences.ShapedListDialog;
import com.byagowi.persiancalendar.view.preferences.ShapedListPreference;

/**
 * Preference activity
 *
 * @author ebraminio
 */
public class ApplicationPreferenceFragment extends PreferenceFragmentCompat {
    private Utils utils;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.settings), "");

        addPreferencesFromResource(R.xml.preferences);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment fragment = null;
        if (preference instanceof PrayerSelectPreference) {
            fragment = new PrayerSelectDialog();
        } else if (preference instanceof GPSLocationPreference) {
            fragment = new GPSLocationDialog();
        } else if (preference instanceof ShapedListPreference) {
            fragment = new ShapedListDialog();
        } else {
            super.onDisplayPreferenceDialog(preference);
        }

        if (fragment != null) {
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            fragment.setArguments(bundle);
            fragment.setTargetFragment(this, 0);
            fragment.show(getChildFragmentManager(), fragment.getClass().getName());
        }
    }
}
