package com.outlay.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.byagowi.persiancalendar.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.outlay.view.activity.BaseActivity;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void enableToolbar(Toolbar toolbar) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDisplayHomeAsUpEnabled(boolean value) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(value);
    }

    public void setTitle(String value) {
        getActivity().setTitle(Html.fromHtml("<small>"+value+"</small>"));
    }

    public View getRootView() {
        return ((BaseActivity)getActivity()).getRootView();
    }
}
