/*
 * Copyright (c) 2016 Metin Kale
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.byagowi.persiancalendar.R;
import com.prayer.settings.Prefs;
import com.prayer.utils.Changelog;
import com.prayer.utils.PermissionUtils;


public abstract class BaseActivity extends AppCompatActivity {
    private static final String[] mActs = {"vakit", "compass", "names", "calendar", "tesbihat", "hadis", "kaza", "zikr", "settings_prayer", "about"};
    private static final int[] ICONS = {R.drawable.ic_menu_times, R.drawable.ic_menu_compass, R.drawable.ic_menu_names,
            R.drawable.ic_menu_calendar, R.drawable.ic_menu_tesbihat, R.drawable.ic_menu_hadith, R.drawable.ic_menu_missed,
            R.drawable.ic_menu_dhikr, R.drawable.ic_menu_settings, R.drawable.ic_menu_about};
    private int mNavPos;


    public BaseActivity() {
        String clz = getClass().toString();
        for (int i = 0; i < mActs.length; i++) {
            if (clz.contains("prayerapp." + mActs[i])) {
                mNavPos = i;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(Prefs.isNightMode() ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);


        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            int id = R.mipmap.ic_launcher;

            Intent.ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this, id);

            Intent intent = new Intent();
            Intent launchIntent = new Intent(this, getClass());
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            launchIntent.putExtra("duplicate", false);

            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getStringArray(R.array.dropdown)[mNavPos]);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

            setResult(RESULT_OK, intent);
            finish();
        }


        if (!Utils.askLang(this)) {
            Utils.init(this);
            Changelog.start(this);
        }

    }

    @Override
    public void setContentView(int res) {
        super.setContentView(R.layout.activity_base_prayer);


        ViewGroup content = (ViewGroup) findViewById(R.id.basecontent);

        View v = LayoutInflater.from(this).inflate(res, content, false);
        content.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (getIntent().getBooleanExtra("anim", false)) {

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected boolean isRTL() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return false;
        }
        Configuration config = getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionUtils.get(this).onRequestPermissionResult(requestCode, permissions, grantResults);
    }


}
