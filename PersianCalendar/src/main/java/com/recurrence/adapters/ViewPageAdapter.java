package com.recurrence.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.byagowi.persiancalendar.R;
import com.recurrence.fragments.TabFragment;
import com.recurrence.utils.ReminderConstants;

public class ViewPageAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.CustomTabProvider {

    private Context mContext;
    private final int[] ICONS = {
            R.drawable.selector_icon_active,
            R.drawable.ic_note_reminder,
            R.drawable.selector_icon_inactive

    };

    public ViewPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public void tabUnselected(View view) {
        view.setSelected(false);
    }

    @Override
    public void tabSelected(View view) {
        view.setSelected(true);
    }

    @Override
    public View getCustomTabView(ViewGroup parent, int position) {
        FrameLayout customLayout = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_tab_recurence, parent, false);
        ((ImageView) customLayout.findViewById(R.id.image)).setImageResource(ICONS[position]);
        return customLayout;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return ICONS.length;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
            default:
                bundle.putInt("TYPE", ReminderConstants.ACTIVE);
                break;
            case 1:
                bundle.putInt("TYPE", ReminderConstants.NOTE);
                break;
            case 2:
                bundle.putInt("TYPE", ReminderConstants.INACTIVE);
                break;

        }
        Fragment fragment = new TabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}