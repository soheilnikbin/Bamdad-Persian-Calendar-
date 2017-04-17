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

package com.prayer.kaza;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.byagowi.persiancalendar.R;
import com.prayer.BaseActivity;


public class Main extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaza_main_prayer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            window.setNavigationBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        ViewGroup vg = (ViewGroup) findViewById(R.id.main);

        int[] ids = {R.string.morningPrayer, R.string.zuhr, R.string.asr, R.string.maghrib, R.string.ishaa, R.string.witr, R.string.fasting};
        for (int i = 0; i < 7; i++) {
            View v = vg.getChildAt(i);
            TextView name = (TextView) v.findViewById(R.id.text);
            final EditText nr = (EditText) v.findViewById(R.id.nr);
            ImageView plus = (ImageView) v.findViewById(R.id.plus);
            ImageView minus = (ImageView) v.findViewById(R.id.minus);

            name.setText(ids[i]);

            plus.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    int i = 0;
                    try {
                        i = Integer.parseInt(nr.getText().toString());
                    } finally {
                        i++;
                    }

                    nr.setText(i + "");

                }
            });

            minus.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = 0;
                    try {
                        i = Integer.parseInt(nr.getText().toString());
                    } finally {
                        i--;
                    }

                    nr.setText(i + "");
                }
            });

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup vg = (ViewGroup) findViewById(R.id.main);
        SharedPreferences prefs = getSharedPreferences("kaza", 0);
        for (int i = 0; i < 7; i++) {
            View v = vg.getChildAt(i);
            EditText nr = (EditText) v.findViewById(R.id.nr);
            nr.setText(prefs.getString("count" + i, "0"));

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ViewGroup vg = (ViewGroup) findViewById(R.id.main);
        SharedPreferences.Editor edit = getSharedPreferences("kaza", 0).edit();
        for (int i = 0; i < 7; i++) {
            View v = vg.getChildAt(i);
            EditText nr = (EditText) v.findViewById(R.id.nr);
            edit.putString("count" + i, nr.getText().toString());

        }

        edit.apply();
    }

}
