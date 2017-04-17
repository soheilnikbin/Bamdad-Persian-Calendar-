package com.recurrence.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.byagowi.persiancalendar.adapter.DrawerAdapter;
import com.byagowi.persiancalendar.adapter.DrawerAdapterRecurrence;
import com.omninotes.MainActivityPep;
import com.prayer.App;
import com.recurrence.adapters.ReminderAdapter;
import com.recurrence.adapters.ViewPageAdapter;
import com.recurrence.receivers.BootReceiver;
import com.byagowi.persiancalendar.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ReminderAdapter.RecyclerListener {

    @Bind(R.id.tabs) PagerSlidingTabStrip mPagerSlidingTabStrip;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.fab_button)
    FloatingActionButton mFloatingActionButton;

    private boolean mFabIsHidden = false;

    private DrawerLayout drawerLayout;
    private DrawerAdapterRecurrence adapter_recurrence;
    FrameLayout appMainView;

    private static final int CALENDAR = 1;
    private static final int TODO = 2;
    private static final int NOTE = 3;
    private static final int MONEY = 4;
    private static final int SETTINGS = 5;
    private static final int EXIT = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recurence);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }
        appMainView = (FrameLayout) findViewById(R.id.recurrence_main_view);

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), getApplicationContext());
        mViewPager.setAdapter(adapter);

        mPagerSlidingTabStrip.setViewPager(mViewPager);
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);

        SharedPreferences sharedPreferences = getSharedPreferences("first_run_preferences", Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && sharedPreferences.getBoolean("FirstRun", true)) {
            sharedPreferences.edit().putBoolean("FirstRun", false).apply();
            Intent intent = new Intent().setClass(this, BootReceiver.class);
            sendBroadcast(intent);
        }

        RecyclerView navigation_recurence = (RecyclerView) findViewById(R.id.navigation_view_recurence);
        navigation_recurence.setHasFixedSize(true);
        adapter_recurrence = new DrawerAdapterRecurrence(this);
        navigation_recurence.setAdapter(adapter_recurrence);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        navigation_recurence.setLayoutManager(layoutManager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_recurrence);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

            int slidingDirection = +1;


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    slidingAnimation(drawerView, slideOffset);
                }
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            private void slidingAnimation(View drawerView, float slideOffset) {
                appMainView.setTranslationX(slideOffset * drawerView.getWidth() * slidingDirection);
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @OnClick(R.id.fab_button)
    public void fabClicked() {
        Intent intent = new Intent(this, CreateEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void hideFab() {
        mFloatingActionButton.hide();
        mFabIsHidden = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFabIsHidden) {
            mFloatingActionButton.show();
            mFabIsHidden = false;
        }
    }


    public void onClickItem(int position) {
        selectItem(position);
    }

    public void selectItem(int item) {

        switch (item){

            case CALENDAR:
                startActivity(new Intent(MainActivity.this, com.byagowi.persiancalendar.view.activity.MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case TODO:
                drawerLayout.closeDrawers();
                break;
            case NOTE:
                startActivity(new Intent(MainActivity.this, MainActivityPep.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case MONEY:
                startActivity(new Intent(MainActivity.this, com.outlay.view.activity.MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case SETTINGS:
                Intent preferenceIntent = new Intent(this, PreferenceActivity.class);
                startActivity(preferenceIntent);
                break;
            case EXIT:
                finish();
                break;
        }

        if (item == 7){
            Toast.makeText(MainActivity.this, "last click",
                    Toast.LENGTH_LONG).show();
        }
    }
}