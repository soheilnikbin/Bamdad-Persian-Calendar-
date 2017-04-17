/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.omninotes;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import calendar.Config;
import de.greenrobot.event.EventBus;

import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.adapter.DrawerAdapter;
import com.byagowi.persiancalendar.adapter.DrawerAdapterPep;
import com.byagowi.persiancalendar.view.activity.MainActivity;
import com.omninotes.async.CategoryMenuTask;
import com.omninotes.async.MainMenuTask;
import com.omninotes.async.bus.CategoriesUpdatedEvent;
import com.omninotes.async.bus.DynamicNavigationReadyEvent;
import com.omninotes.async.bus.NavigationUpdatedEvent;
import com.omninotes.async.bus.NavigationUpdatedNavDrawerClosedEvent;
import com.omninotes.async.bus.NotesLoadedEvent;
import com.omninotes.async.bus.NotesUpdatedEvent;
import com.omninotes.async.bus.SwitchFragmentEvent;
import com.omninotes.models.Category;
import com.omninotes.models.NavigationItem;
import com.omninotes.utils.Constants;
import com.omninotes.utils.Display;


public class NavigationDrawerFragment extends Fragment {
    static final int BURGER = 0;
    static final int ARROW = 1;

    private static final int CALENDAR = 1;
    private static final int TODO = 2;
    private static final int NOTE = 3;
    private static final int MONEY = 4;
    int slidingDirection = +1;

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    private MainActivityPep mActivity;
    private boolean alreadyInitialized;
    RecyclerView navigation;
    private DrawerAdapterPep adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer_pep, container, false);

        navigation = (RecyclerView) view.findViewById(R.id.navigation_view_pep);
        adapter = new DrawerAdapterPep(this);
        navigation.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        navigation.setLayoutManager(layoutManager);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (MainActivityPep) getActivity();

    }


    private MainActivityPep getMainActivity() {
        return (MainActivityPep) getActivity();
    }


    public void onEventMainThread(DynamicNavigationReadyEvent event) {
        if (alreadyInitialized) {
            alreadyInitialized = false;
        } else {
            init();
        }
    }


    public void onEvent(CategoriesUpdatedEvent event) {
        init();
    }


    public void onEventAsync(NotesUpdatedEvent event) {
        alreadyInitialized = false;
    }


    public void onEvent(NotesLoadedEvent event) {
        if (mDrawerLayout != null) {
            if (!isDoublePanelActive()) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }
        init();
        alreadyInitialized = true;
    }


    public void onEvent(SwitchFragmentEvent event) {
        switch (event.direction) {
            case CHILDREN:
                animateBurger(ARROW);
                break;
            default:
                animateBurger(BURGER);
        }
    }


    public void onEvent(NavigationUpdatedEvent navigationUpdatedEvent) {
        if (navigationUpdatedEvent.navigationItem.getClass().isAssignableFrom(NavigationItem.class)) {
            mActivity.getSupportActionBar().setTitle(((NavigationItem) navigationUpdatedEvent.navigationItem).getText());
        } else {
            mActivity.getSupportActionBar().setTitle(((Category) navigationUpdatedEvent.navigationItem).getName());
        }
        if (mDrawerLayout != null) {
            if (!isDoublePanelActive()) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            new Handler().postDelayed(() -> EventBus.getDefault().post(new NavigationUpdatedNavDrawerClosedEvent
                    (navigationUpdatedEvent.navigationItem)), 400);
        }
    }


    public void init() {
        Log.d(Constants.TAG, "Started navigation drawer initialization");

        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mDrawerLayout.setFocusableInTouchMode(false);

        // Setting specific bottom margin for Kitkat with translucent nav bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View leftDrawer = getView().findViewById(R.id.left_drawer);
            int leftDrawerBottomPadding = Display.getNavigationBarHeightKitkat(getActivity());
            leftDrawer.setPadding(leftDrawer.getPaddingLeft(), leftDrawer.getPaddingTop(),
                    leftDrawer.getPaddingRight(), leftDrawerBottomPadding);
        }

        buildMainMenu();
        Log.d(Constants.TAG, "Finished main menu initialization");
        buildCategoriesMenu();
        Log.d(Constants.TAG, "Finished categories menu initialization");

        // ActionBarDrawerToggle± ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(mActivity,
                mDrawerLayout,
                getMainActivity().getToolbar(),
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                mActivity.supportInvalidateOptionsMenu();
            }


            public void onDrawerOpened(View drawerView) {
                mActivity.commitPending();
                mActivity.finishActionMode();
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
                if(Config.animateMenu == true){
                    MainActivityPep.relativeLayout.setTranslationX(slideOffset * drawerView.getWidth() * slidingDirection);
                    mDrawerLayout.bringChildToFront(drawerView);
                    mDrawerLayout.requestLayout();
                }
            }
        };

        if (isDoublePanelActive()) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }

        // Styling options
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        Log.d(Constants.TAG, "Finished navigation drawer initialization");
    }



    private void buildCategoriesMenu() {
        CategoryMenuTask task = new CategoryMenuTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void buildMainMenu() {
        MainMenuTask task = new MainMenuTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    void animateBurger(int targetShape) {
        if (mDrawerToggle != null) {
            if (targetShape != BURGER && targetShape != ARROW)
                return;
            ValueAnimator anim = ValueAnimator.ofFloat((targetShape + 1) % 2, targetShape);
            anim.addUpdateListener(valueAnimator -> {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset);
            });
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(500);
            anim.start();
        }
    }


    public static boolean isDoublePanelActive() {
//		Resources resources = OmniNotes.getAppContext().getResources();
//		return resources.getDimension(R.dimen.navigation_drawer_width) == resources.getDimension(R.dimen
//				.navigation_drawer_reserved_space);
        return false;
    }


    public void selectItem(int item) {

        switch (item){

            case CALENDAR:
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                getActivity().finish();
                break;
            case TODO:
                startActivity(new Intent(getActivity(), com.recurrence.activities.MainActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                getActivity().finish();
                break;
            case NOTE:
                startActivity(new Intent(getActivity(), MainActivityPep.class));
                break;
            case MONEY:
                startActivity(new Intent(getActivity(), com.outlay.view.activity.MainActivity.class));
                getActivity().finish();
                break;
        }

        if (item == CALENDAR || item == TODO){

        }else{
            mDrawerLayout.closeDrawers();
        }

    }
    public void onClickItem(int position) {
        selectItem(position);
    }
}
