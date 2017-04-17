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

package com.prayer.vakit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import com.getbase.floatingactionbutton.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byagowi.persiancalendar.R;;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.omninotes.models.Note;
import com.omninotes.models.views.Fab;
import com.omninotes.utils.Constants;
import com.prayer.App;
import com.prayer.BaseActivity;
import com.prayer.HicriDate;
import com.prayer.Utils;
import com.prayer.settings.Prefs;
import com.prayer.utils.MultipleOrientationSlidingDrawer;
import com.prayer.utils.RTLViewPager;
import com.prayer.vakit.fragments.ImsakiyeFragment;
import com.prayer.vakit.fragments.MainFragment;
import com.prayer.vakit.fragments.SettingsFragment;
import com.prayer.vakit.fragments.SortFragment;
import com.prayer.vakit.times.Times;

public class Main extends BaseActivity implements OnPageChangeListener, View.OnClickListener {

    public static boolean isRunning;
    public MyAdapter mAdapter;
    private RTLViewPager mPager;
    private int mStartPos = 1;
    private SettingsFragment mSettingsFrag;
    private ImsakiyeFragment mImsakiyeFrag;
    public static  MultipleOrientationSlidingDrawer mTopSlider;

    public static PendingIntent getPendingIntent(Times t) {
        if (t == null) {
            return null;
        }
        Context context = App.getContext();
        Intent intent = new Intent(context, Main.class);
        intent.putExtra("startCity", Times.getTimes().indexOf(t));
        return PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vakit_main_prayer);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            window.setNavigationBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        Prefs.setLanguage("en");
        initFab();

        if (App.getContext() == null) {
            App.setContext(this);
        }

        mPager = (RTLViewPager) findViewById(R.id.pager);
        mAdapter = new MyAdapter(getSupportFragmentManager());

        mSettingsFrag = (SettingsFragment) getFragmentManager().findFragmentByTag("settings");
        mImsakiyeFrag = (ImsakiyeFragment) getFragmentManager().findFragmentByTag("imsakiye");

        mTopSlider = (MultipleOrientationSlidingDrawer) findViewById(R.id.topSlider);

        mPager.setRTLSupportAdapter(getSupportFragmentManager(), mAdapter);

//        int holyday = HicriDate.isHolyday();
//        if (holyday != 0) {
//            TextView tv = (TextView) findViewById(R.id.holyday);
//            tv.setVisibility(View.VISIBLE);
//            tv.setText(Utils.getHolyday(holyday - 1));
//        }

        onNewIntent(getIntent());
        mPager.addOnPageChangeListener(this);


        mTopSlider.setOnDrawerScrollListener(new MultipleOrientationSlidingDrawer.OnDrawerScrollListener() {
            @Override
            public void onScrollStarted() {
            }

            @Override
            public void onScrolling(int pos) {
                mPager.setTranslationY(pos);
            }

            @Override
            public void onScrollEnded() {

            }
        });

        mTopSlider.setOnDrawerOpenListener(new MultipleOrientationSlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                int position = mPager.getCurrentItem();
                if (position != 0) {
                    Times t = Times.getTimes(mAdapter.getItemId(position));
                    Log.e("TIME ERROR"," "+t);
                    mSettingsFrag.setTimes(t);
                }
            }
        });

        mTopSlider.setOnDrawerCloseListener(new MultipleOrientationSlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {


                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + mPager.getId() + ":" + mAdapter.getItemId(mPager.getCurrentItem()));
                if (page instanceof MainFragment) {
                    ((MainFragment) page).update();
                }

            }
        });
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

    private void initFab() {
        RelativeLayout overlay = (RelativeLayout) findViewById(R.id.overlayBackground);
        FloatingActionsMenu fab = (FloatingActionsMenu) findViewById(R.id.fabPrayer);
        overlay.setOnClickListener(view -> fab.collapse());
        fab.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                overlay.setVisibility(View.GONE);
            }
        });
        FloatingActionButton f1 = (FloatingActionButton) findViewById(R.id.fab_missedPrayer);
        FloatingActionButton f2 = (FloatingActionButton) findViewById(R.id.fab_ghotbnama);
        FloatingActionButton f3 = (FloatingActionButton) findViewById(R.id.fab_tasbih);
        FloatingActionButton f4 = (FloatingActionButton) findViewById(R.id.fab_addPrayer);
        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this, AddCity.class));
                fab.collapse();
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this, com.compass.compass.Main.class));
                fab.collapse();
            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this, com.prayer.zikr.Main.class));
                fab.collapse();
            }
        });
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this, com.prayer.kaza.Main.class));
                fab.collapse();
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mStartPos = getIntent().getIntExtra("startCity", -1) + 1;
        if (mStartPos <= 0) {
            mStartPos = 1;
        }
        mStartPos = Math.min(mStartPos, mAdapter.getCount() - 1);

        mPager.setCurrentItem(mStartPos);
        onPageScrolled(mStartPos, 0, 0);
        onPageSelected(mStartPos);
        onPageScrollStateChanged(ViewPager.SCROLL_STATE_IDLE);
    }

    @Override
    public void onClick(View v) {
//        if (v == mAddCityFab) {
//            startActivity(new Intent(this, AddCity.class));
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
        Times.addOnTimesListChangeListener(mAdapter);


    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
        Times.removeOnTimesListChangeListener(mAdapter);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag("notPrefs");
            if (frag != null) {
                getSupportFragmentManager().beginTransaction().remove(frag).commit();
                return true;
            } else if (mTopSlider.isOpened()) {
                mTopSlider.animateClose();
                return true;
            }


        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            int pos = mPager.getCurrentItem();
            if (pos != 0) {
                Times t = Times.getTimes(mAdapter.getItemId(pos));
//                mImsakiyeFrag.setTimes(t);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//        if (mAddCityFab != null) if ((position == 0) && (positionOffset == 0)) {
//            mAddCityFab.show();
//        } else {
//            mAddCityFab.hide();
//        }


    }


    @Override
    public void onPageSelected(int pos) {

    }

    public void onItemClick(int pos) {
        mPager.setCurrentItem(pos + 1, true);

    }

    public class MyAdapter extends FragmentPagerAdapter implements Times.OnTimesListChangeListener {

        public MyAdapter(FragmentManager fm) {
            super(fm);

        }


        @Override
        public int getCount() {
            return Times.getCount() + 1;
        }

        @Override
        public long getItemId(int position) {
            if (position == 0) {
                return 0;
            }

            return Times.getTimesAt(position - 1).getID();
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof SortFragment) {
                return 0;
            } else {
                MainFragment frag = (MainFragment) object;
                int pos = Times.getTimes().indexOf(frag.getTimes());
                if (pos >= 0) {
                    return pos + 1;
                }
            }
            return POSITION_NONE;
        }


        @Override
        public Fragment getItem(int position) {
            if (position > 0) {
                MainFragment frag = new MainFragment();
                Bundle bdl = new Bundle();
                bdl.putLong("city", getItemId(position));
                frag.setArguments(bdl);

                if (position == mStartPos) {
                    mStartPos = 0;
                }
                return frag;
            } else {
                return new SortFragment();
            }
        }


    }
}
