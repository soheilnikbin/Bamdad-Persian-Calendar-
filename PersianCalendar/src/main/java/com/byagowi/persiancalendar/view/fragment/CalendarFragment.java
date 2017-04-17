package com.byagowi.persiancalendar.view.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.byagowi.persiancalendar.Constants;
import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.adapter.CalendarAdapter;
import com.byagowi.persiancalendar.util.Utils;
import com.byagowi.persiancalendar.view.activity.ConvertPersianCal;
import com.byagowi.persiancalendar.view.activity.MainActivity;
import com.byagowi.persiancalendar.view.dialog.SelectDayDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import calendar.CivilDate;
import calendar.Config;
import calendar.DateConverter;
import calendar.ForecastConfig;
import calendar.PersianDate;
import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

public class CalendarFragment extends Fragment
        implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager monthViewPager;
    private Utils utils;

    private Calendar calendar = Calendar.getInstance();

    private TextView weekDayName;
    private TextView gregorianDate;
    private TextView islamicDate;
    private TextView shamsiDate;
    private TextView eventTitle;
    private TextView holidayTitle;
    private String clickURL = "";

    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

    private LinearLayout info_message_layout;
    private ImageView info_message_icon;
    private TextView info_message_text;
    private CardView update_view;


    private CardView owghat;
    private CardView event;

    private int viewPagerPosition;

    public static Space space;
    String CITY;
    String temp_inorder;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        utils = Utils.getInstance(getContext());
        viewPagerPosition = 0;

        info_message_layout = (LinearLayout) view.findViewById(R.id.info_message);
        info_message_icon = (ImageView) view.findViewById(R.id.info_message_icon);
        info_message_text = (TextView) view.findViewById(R.id.info_message_text);
        update_view = (CardView) view.findViewById(R.id.updateView);
        update_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/details?id=com.vmagnify.persiancalendar";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().build());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("info_message_pref", MODE_PRIVATE).edit();

        HashMap<String, Object> defaults_if_message_is_on = new HashMap<>();
        defaults_if_message_is_on.put("info_message_is_on",false);
        remoteConfig.setDefaults(defaults_if_message_is_on);
        final Task<Void> fetch = remoteConfig.fetch(12);
        fetch.addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                remoteConfig.activateFetched();
                boolean boolVal = (boolean) remoteConfig.getBoolean("info_message_is_on");
                if(boolVal == true){
                    editor.putBoolean("info_message_is_on",true);
                    editor.commit();
                    info_message_layout.setVisibility(View.VISIBLE);
                }else{
                    editor.putBoolean("info_message_is_on",false);
                    editor.commit();
                    info_message_layout.setVisibility(View.GONE);
                }
            }
        });

        HashMap<String, Object> defaults_updateView = new HashMap<>();
        defaults_updateView.put("update_parameter","1");
        remoteConfig.setDefaults(defaults_updateView);
        final Task<Void> fetch6 = remoteConfig.fetch(12);
        fetch6.addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //write your logic for Firebase remote configuration
            }
        });

        HashMap<String, Object> defaults_info_click_url = new HashMap<>();
        defaults_info_click_url.put("info_click_url","");
        remoteConfig.setDefaults(defaults_info_click_url);
        final Task<Void> fetch5 = remoteConfig.fetch(12);
        fetch5.addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                remoteConfig.activateFetched();
                String click_url = (String) remoteConfig.getString("info_click_url");
                clickURL = click_url;
                editor.putString("info_click_url",click_url);
                editor.commit();
            }
        });




        space = (Space) view.findViewById(R.id.space);
        gregorianDate = (TextView) view.findViewById(R.id.gregorian_date);
        utils.setFont(gregorianDate);
        islamicDate = (TextView) view.findViewById(R.id.islamic_date);
        utils.setFont(islamicDate);
        shamsiDate = (TextView) view.findViewById(R.id.shamsi_date);
        utils.setFont(shamsiDate);
        weekDayName = (TextView) view.findViewById(R.id.week_day_name);
        utils.setFont(weekDayName);

        eventTitle = (TextView) view.findViewById(R.id.event_title);
        utils.setFont(eventTitle);
        holidayTitle = (TextView) view.findViewById(R.id.holiday_title);
        utils.setFont(holidayTitle);

        owghat = (CardView) view.findViewById(R.id.owghat);
        event = (CardView) view.findViewById(R.id.cardEvent);

        monthViewPager = (ViewPager) view.findViewById(R.id.calendar_pager);

        monthViewPager.setAdapter(new CalendarAdapter(getChildFragmentManager()));
        monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);

        monthViewPager.addOnPageChangeListener(this);

        owghat.setOnClickListener(this);
        MainActivity.backToMainDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bringTodayYearMonth();
            }
        });
        gregorianDate.setOnClickListener(this);
        islamicDate.setOnClickListener(this);
        shamsiDate.setOnClickListener(this);

        utils.setFontAndShape((TextView) view.findViewById(R.id.event_card_title));

        String cityName = utils.getGeoCodedCityName();


        // This will immediately be replaced by the same functionality on fragment but is here to
        // make sure enough space is dedicated to actionbar's title and subtitle, kinda hack anyway
        PersianDate today = utils.getToday();
        utils.setActivityTitleAndSubtitle(getActivity(), utils.getMonthName(today),
                utils.formatNumber(today.getYear()));

        return view;
    }

    public void changeMonth(int position) {
        monthViewPager.setCurrentItem(monthViewPager.getCurrentItem() + position, true);
    }

    public void selectDay(PersianDate persianDate) {
        weekDayName.setText(utils.shape(utils.getWeekDayName(persianDate)));
        shamsiDate.setText(utils.shape(utils.dateToString(persianDate)));
        CivilDate civilDate = DateConverter.persianToCivil(persianDate);
        gregorianDate.setText(utils.shape(utils.dateToString(civilDate)));
        islamicDate.setText(utils.shape(utils.dateToString(
                DateConverter.civilToIslamic(civilDate, utils.getIslamicOffset()))));

        if (utils.getToday().equals(persianDate)) {
            MainActivity.hideFab();
            MainActivity.showPanel();
            if (utils.iranTime) {
                weekDayName.setText(weekDayName.getText() +
                        utils.shape(" (" + getString(R.string.iran_time) + ")"));
            }
        } else {
            MainActivity.showFab();
            MainActivity.hidePanel();
        }
        showEvent(persianDate);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void addEventOnCalendar(PersianDate persianDate) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        CivilDate civil = DateConverter.persianToCivil(persianDate);

        intent.putExtra(CalendarContract.Events.DESCRIPTION,
                utils.dayTitleSummary(persianDate));

        Calendar time = Calendar.getInstance();
        time.set(civil.getYear(), civil.getMonth() - 1, civil.getDayOfMonth());

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                time.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                time.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        startActivity(intent);
    }

    private void showEvent(PersianDate persianDate) {
        String holidays = utils.getEventsTitle(persianDate, true);
        String events = utils.getEventsTitle(persianDate, false);

        event.setVisibility(View.GONE);
        holidayTitle.setVisibility(View.GONE);
        eventTitle.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(holidays)) {
            holidayTitle.setText(utils.shape(holidays));
            holidayTitle.setVisibility(View.VISIBLE);
            event.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(events)) {
            eventTitle.setText(utils.shape(events));
            eventTitle.setVisibility(View.VISIBLE);
            event.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.islamic_date:
            case R.id.shamsi_date:
            case R.id.gregorian_date:
                utils.copyToClipboard(v);
                break;
        }
    }

    private void bringTodayYearMonth() {
        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT,
                Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        if (monthViewPager.getCurrentItem() != Constants.MONTHS_LIMIT / 2) {
            monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);
        }

        selectDay(utils.getToday());
    }

    public void bringDate(PersianDate date) {
        PersianDate today = utils.getToday();
        viewPagerPosition =
                (today.getYear() - date.getYear()) * 12 + today.getMonth() - date.getMonth();

        monthViewPager.setCurrentItem(viewPagerPosition + Constants.MONTHS_LIMIT / 2);

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, viewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, date.getDayOfMonth());

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        selectDay(date);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        viewPagerPosition = position - Constants.MONTHS_LIMIT / 2;

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, viewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        MainActivity.showFab();
        MainActivity.hidePanel();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.action_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to:
                SelectDayDialog dialog = new SelectDayDialog();
                dialog.show(getChildFragmentManager(), SelectDayDialog.class.getName());
                break;
            default:
                break;
        }
        return true;
    }

    public int getViewPagerPosition() {
        return viewPagerPosition;
    }


    public static void hideSpace(){
        space.setVisibility(View.GONE);
    }
    public static void showSpace(){
        try {
            space.setVisibility(View.VISIBLE);
        }catch(Exception e){}
    }
}
