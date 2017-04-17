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

package com.omninotes.async;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.view.activity.MainActivity;
import com.omninotes.MainActivityPep;
import com.omninotes.SettingsActivity;
import com.omninotes.async.bus.NavigationUpdatedEvent;
import com.omninotes.db.DbHelper;
import com.omninotes.models.Category;
import com.omninotes.models.ONStyle;
import com.omninotes.models.adapters.NavDrawerCategoryAdapter;
import com.omninotes.models.views.NonScrollableListView;
import java.lang.ref.WeakReference;
import java.util.List;

import de.greenrobot.event.EventBus;


public class CategoryMenuTask extends AsyncTask<Void, Void, List<Category>> {

    private final WeakReference<Fragment> mFragmentWeakReference;
    private final MainActivityPep mainActivityPep;
    private NonScrollableListView mDrawerCategoriesList;
    private View settingsView , exitView;
    private View settingsViewCat;
    private NonScrollableListView mDrawerList;
    RelativeLayout categoryHeader;


    public CategoryMenuTask(Fragment mFragment) {
        mFragmentWeakReference = new WeakReference<>(mFragment);
        this.mainActivityPep = (MainActivityPep) mFragment.getActivity();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDrawerList = (NonScrollableListView) mainActivityPep.findViewById(R.id.drawer_nav_list);
        LayoutInflater inflater = (LayoutInflater) mainActivityPep.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        settingsView = mainActivityPep.findViewById(R.id.settings_view);
        categoryHeader = (RelativeLayout) mainActivityPep.findViewById(R.id.category_header);
        exitView = mainActivityPep.findViewById(R.id.exit_view);
        exitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityPep.finish();
            }
        });

        // Settings view when categories are available
        mDrawerCategoriesList = (NonScrollableListView) mainActivityPep.findViewById(R.id.drawer_tag_list);
        if (mDrawerCategoriesList.getAdapter() == null && mDrawerCategoriesList.getFooterViewsCount() == 0) {
            settingsViewCat = inflater.inflate(R.layout.drawer_category_list_footer_pep, null);
            mDrawerCategoriesList.addFooterView(settingsViewCat);
        } else {
            settingsViewCat = mDrawerCategoriesList.getChildAt(mDrawerCategoriesList.getChildCount() - 1);
        }

    }


    @Override
    protected List<Category> doInBackground(Void... params) {
        if (isAlive()) {
            return buildCategoryMenu();
        } else {
            cancel(true);
            return null;
        }
    }


    @Override
    protected void onPostExecute(final List<Category> categories) {
        if (isAlive()) {
            mDrawerCategoriesList.setAdapter(new NavDrawerCategoryAdapter(mainActivityPep, categories,
                    mainActivityPep.getNavigationTmp()));
            if (categories.size() == 0) {
                setWidgetVisibility(settingsViewCat, false);
                setWidgetVisibility(settingsView, true);
                setWidgetVisibility(exitView,true);
                categoryHeader.setVisibility(View.GONE);
            } else {
                setWidgetVisibility(settingsViewCat, true);
                setWidgetVisibility(settingsView, false);
                setWidgetVisibility(exitView,false);
                categoryHeader.setVisibility(View.VISIBLE);
            }
            mDrawerCategoriesList.justifyListViewHeightBasedOnChildren();

        }
    }


    private void setWidgetVisibility(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }


    private boolean isAlive() {
        return mFragmentWeakReference.get() != null
                && mFragmentWeakReference.get().isAdded()
                && mFragmentWeakReference.get().getActivity() != null
                && !mFragmentWeakReference.get().getActivity().isFinishing();
    }


    private List<Category> buildCategoryMenu() {
        // Retrieves data to fill tags list
        List<Category> categories = DbHelper.getInstance().getCategories();

        View settings = categories.isEmpty() ? settingsView : settingsViewCat;
        if (settings == null) return categories;
//        Fonts.overrideTextSize(mainActivity,
//                mainActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS),
//                settings_pep);
        settings.setOnClickListener(v -> {
			Intent settingsIntent = new Intent(mainActivityPep, SettingsActivity.class);
			mainActivityPep.startActivity(settingsIntent);
		});

        // Sets click events
        mDrawerCategoriesList.setOnItemClickListener((arg0, arg1, position, arg3) -> {

			Object item = mDrawerCategoriesList.getAdapter().getItem(position);
			// Ensuring that clicked item is not the ListView header
			if (item != null ) {
                EventBus.getDefault().post(new NavigationUpdatedEvent(mDrawerCategoriesList.getItemAtPosition(position)));
                mainActivityPep.updateNavigation(String.valueOf(((Category) item).getId()));
                mDrawerCategoriesList.setItemChecked(position, true);
                // Forces redraw
                if (mDrawerList != null) {
                    mDrawerList.setItemChecked(0, false);
                    EventBus.getDefault().post(new NavigationUpdatedEvent(mDrawerCategoriesList.getItemAtPosition
                            (position)));
                }
			}
		});

        // Sets long click events
        mDrawerCategoriesList.setOnItemLongClickListener((arg0, view, position, arg3) -> {
			if (mDrawerCategoriesList.getAdapter() != null) {
				Object item = mDrawerCategoriesList.getAdapter().getItem(position);
				// Ensuring that clicked item is not the ListView header
				if (item != null) {
					mainActivityPep.editTag((Category) item);
				}
			} else {
				mainActivityPep.showMessage(R.string.category_deleted, ONStyle.ALERT);
			}
			return true;
		});

        return categories;
    }

}