package com.recurrence.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.util.Utils;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.recurrence.database.DatabaseHelper;
import com.recurrence.dialogs.AdvancedRepeatSelector;
import com.recurrence.dialogs.DaysOfWeekSelector;
import com.recurrence.dialogs.IconPicker;
import com.recurrence.dialogs.RepeatSelector;
import com.recurrence.models.Colour;
import com.recurrence.models.Reminder;
import com.recurrence.receivers.AlarmReceiver;
import com.recurrence.utils.AlarmUtil;
import com.recurrence.utils.AnimationUtil;
import com.recurrence.utils.DateAndTimeUtil;
import com.recurrence.utils.ReminderConstants;
import com.recurrence.utils.TextFormatUtil;


import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;

import java.io.Console;
import java.util.Calendar;


import butterknife.Bind;
import butterknife.ButterKnife;

import butterknife.OnClick;
import calendar.CivilDate;
import calendar.DateConverter;
import calendar.PersianDate;

public class CreateEditActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback,
        IconPicker.IconSelectionListener, AdvancedRepeatSelector.AdvancedRepeatSelectionListener,
        DaysOfWeekSelector.DaysOfWeekSelectionListener, RepeatSelector.RepeatSelectionListener,
        TimePickerDialog.OnTimeSetListener,
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    @Bind(R.id.create_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.notification_title) EditText mTitleEditText;
    @Bind(R.id.notification_content) EditText mContentEditText;
    @Bind(R.id.time) TextView mTimeText;
    @Bind(R.id.date) TextView mDateText;
    @Bind(R.id.repeat_day) TextView mRepeatText;
    @Bind(R.id.switch_toggle)
    SwitchCompat mForeverSwitch;
    @Bind(R.id.show_times_number) EditText mTimesEditText;
    @Bind(R.id.forever_row) LinearLayout mForeverRow;
    @Bind(R.id.bottom_row) LinearLayout mBottomRow;
    @Bind(R.id.bottom_view) View mBottomView;
    @Bind(R.id.show) TextView mShowText;
    @Bind(R.id.times) TextView mTimesText;
    @Bind(R.id.select_icon_text) TextView mIconText;
    @Bind(R.id.select_colour_text) TextView mColourText;
    @Bind(R.id.colour_icon) ImageView mImageColourSelect;
    @Bind(R.id.selected_icon) ImageView mImageIconSelect;
    @Bind(R.id.error_time) ImageView mImageWarningTime;
    @Bind(R.id.error_date) ImageView mImageWarningDate;
    @Bind(R.id.error_show) ImageView mImageWarningShow;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private String mIcon;
    private String mColour;
    private Calendar mCalendar;
    private boolean[] mDaysOfWeek = new boolean[7];
    private int mTimesShown = 0;
    private int mTimesToShow = 1;
    private int mRepeatType;
    private int mId;
    private int mInterval = 1;

    private static final String TIMEPICKER = "TimePickerDialog",
            DATEPICKER = "DatePickerDialog";

    CivilDate civilDate = null;
    CivilDate civilDateintro = null;

    PersianDate persianDate, persianDateintro;
    private Utils utils;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_recurence);
            ButterKnife.bind(this);

            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
            if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(null);

            mCalendar = Calendar.getInstance();
            utils = Utils.getInstance(getBaseContext());

            mIcon = getString(R.string.default_icon_value);
            mColour = getString(R.string.default_colour_value);
            mRepeatType = ReminderConstants.DOES_NOT_REPEAT;
            mId = getIntent().getIntExtra("NOTIFICATION_ID", 0);

            // Check whether to edit or create a new notification
            if (mId == 0) {
                DatabaseHelper database = DatabaseHelper.getInstance(this);
                mId = database.getLastNotificationId() + 1;
                database.close();
            } else {
                assignReminderValues();
            }
        }

    public void assignReminderValues() {
        // Prevent keyboard from opening automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DatabaseHelper database = DatabaseHelper.getInstance(this);
        Reminder reminder = database.getNotification(mId);
        database.close();

        mTimesShown = reminder.getNumberShown();
        mRepeatType = reminder.getRepeatType();
        mInterval = reminder.getInterval();
        mIcon = reminder.getIcon();
        mColour = reminder.getColour();

        mCalendar = DateAndTimeUtil.parseDateAndTime(reminder.getDateAndTime());

        mShowText.setText(getString(R.string.times_shown_edit, reminder.getNumberShown()));
        mTitleEditText.setText(reminder.getTitle());
        mContentEditText.setText(reminder.getContent());

        int a = mCalendar.get(Calendar.YEAR);
        int b = mCalendar.get(Calendar.MONTH)+1;
        int c = mCalendar.get(Calendar.DAY_OF_MONTH);
        Log.e("year"," "+a);
        Log.e("month"," "+b);
        Log.e("day"," "+c);
        civilDateintro = new CivilDate(a,b,c);
        persianDateintro = DateConverter.civilToPersian(civilDateintro);
        mDateText.setText(utils.shape(utils.dateToString(persianDateintro)));

        mTimeText.setText(DateAndTimeUtil.toStringReadableTime(mCalendar, this));
        mTimesEditText.setText(String.valueOf(reminder.getNumberToShow()));
        mColourText.setText(mColour);
        mImageColourSelect.setColorFilter(Color.parseColor(mColour));
        mTimesText.setVisibility(View.VISIBLE);

        if (!getString(R.string.default_icon).equals(mIcon)) {
            mImageIconSelect.setImageResource(getResources().getIdentifier(reminder.getIcon(), "drawable", getPackageName()));
            mIconText.setText(R.string.custom_icon);
        }

        if (reminder.getRepeatType() != ReminderConstants.DOES_NOT_REPEAT) {
            if (reminder.getInterval() > 1) {
                mRepeatText.setText(TextFormatUtil.formatAdvancedRepeatText(this, mRepeatType, mInterval));
            } else {
                mRepeatText.setText(getResources().getStringArray(R.array.repeat_array)[reminder.getRepeatType()]);
            }
            showFrequency(true);
        }

        if (reminder.getRepeatType() == ReminderConstants.SPECIFIC_DAYS) {
            mDaysOfWeek = reminder.getDaysOfWeek();
            mRepeatText.setText(TextFormatUtil.formatDaysOfWeekText(this, mDaysOfWeek));
        }

        if (Boolean.parseBoolean(reminder.getForeverState())) {
            mForeverSwitch.setChecked(true);
            mBottomRow.setVisibility(View.GONE);
        }
    }

    public void showFrequency(boolean show) {
        if (show) {
            mForeverRow.setVisibility(View.VISIBLE);
            mBottomRow.setVisibility(View.VISIBLE);
            mBottomView.setVisibility(View.VISIBLE);
        } else {
            mForeverSwitch.setChecked(false);
            mForeverRow.setVisibility(View.GONE);
            mBottomRow.setVisibility(View.GONE);
            mBottomView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.time_row)
    public void timePicker() {

        PersianCalendar now = new PersianCalendar();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                CreateEditActivity.this,
                now.get(PersianCalendar.HOUR_OF_DAY),
                now.get(PersianCalendar.MINUTE),
                true
        );
        tpd.setThemeDark(true);
        tpd.show(getFragmentManager(), TIMEPICKER);
    }

    @OnClick(R.id.date_row)
    public void datePicker(View view) {
        PersianCalendar now = new PersianCalendar();
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog dpd = com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.newInstance(
                CreateEditActivity.this,
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay());
        dpd.setThemeDark(true);

        dpd.show(getFragmentManager(), DATEPICKER);
    }

    @OnClick(R.id.icon_select)
    public void iconSelector() {
        DialogFragment dialog = new IconPicker();
        dialog.show(getSupportFragmentManager(), "IconPicker");
    }

    @Override
    public void onIconSelection(DialogFragment dialog, String iconName, String iconType, int iconResId) {
        mIcon = iconName;
        mIconText.setText(iconType);
        mImageIconSelect.setImageResource(iconResId);
        dialog.dismiss();
    }

    @OnClick(R.id.colour_select)
    public void colourSelector() {
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        int[] colours = database.getColoursArray();
        database.close();

        new ColorChooserDialog.Builder(this, R.string.select_colour)
                .allowUserColorInputAlpha(false)
                .customColors(colours, null)
                .preselect(Color.parseColor(mColour))
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColour) {
        mColour = String.format("#%06X", (0xFFFFFF & selectedColour));
        mImageColourSelect.setColorFilter(selectedColour);
        mColourText.setText(mColour);
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        database.addColour(new Colour(selectedColour, DateAndTimeUtil.toStringDateTimeWithSeconds(Calendar.getInstance())));
        database.close();
    }

    @OnClick(R.id.repeat_row)
    public void repeatSelector() {
        DialogFragment dialog = new RepeatSelector();
        dialog.show(getSupportFragmentManager(), "RepeatSelector");
    }

    @Override
    public void onRepeatSelection(DialogFragment dialog, int which, String repeatText) {
        mInterval = 1;
        mRepeatType = which;
        mRepeatText.setText(repeatText);
        if (which == ReminderConstants.DOES_NOT_REPEAT) {
            showFrequency(false);
        } else {
            showFrequency(true);
        }
    }

    @Override
    public void onDaysOfWeekSelected(boolean[] days) {
        mRepeatText.setText(TextFormatUtil.formatDaysOfWeekText(this, days));
        mDaysOfWeek = days;
        mRepeatType = ReminderConstants.SPECIFIC_DAYS;
        showFrequency(true);
    }

    @Override
    public void onAdvancedRepeatSelection(int type, int interval, String repeatText) {
        mRepeatType = type;
        mInterval = interval;
        mRepeatText.setText(repeatText);
        showFrequency(true);
    }

    public void saveNotification() {
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        Reminder reminder = new Reminder()
                .setId(mId)
                .setTitle(mTitleEditText.getText().toString())
                .setContent(mContentEditText.getText().toString())
                .setDateAndTime(DateAndTimeUtil.toStringDate(mCalendar) + DateAndTimeUtil.toStringTime(mCalendar))
                .setRepeatType(mRepeatType)
                .setForeverState(Boolean.toString(mForeverSwitch.isChecked()))
                .setNumberToShow(mTimesToShow)
                .setNumberShown(mTimesShown)
                .setIcon(mIcon)
                .setColour(mColour)
                .setInterval(mInterval);

        database.addNotification(reminder);

        if (mRepeatType == ReminderConstants.SPECIFIC_DAYS) {
            reminder.setDaysOfWeek(mDaysOfWeek);
            database.addDaysOfWeek(reminder);
        }

        database.close();
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        mCalendar.set(Calendar.SECOND, 0);
        AlarmUtil.setAlarm(this, alarmIntent, reminder.getId(), mCalendar);
        finish();
    }

    @OnClick(R.id.forever_row)
    public void toggleSwitch() {
        mForeverSwitch.toggle();
        if (mForeverSwitch.isChecked()) {
            mBottomRow.setVisibility(View.GONE);
        } else {
            mBottomRow.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.switch_toggle)
    public void switchClicked() {
        if (mForeverSwitch.isChecked()) {
            mBottomRow.setVisibility(View.GONE);
        } else {
            mBottomRow.setVisibility(View.VISIBLE);
        }
    }

    public void validateInput() {
        mImageWarningShow.setVisibility(View.GONE);
        mImageWarningTime.setVisibility(View.GONE);
        mImageWarningDate.setVisibility(View.GONE);
        Calendar nowCalendar = Calendar.getInstance();

        if (mTimeText.getText().equals(getString(R.string.time_now))) {
            mCalendar.set(Calendar.HOUR_OF_DAY, nowCalendar.get(Calendar.HOUR_OF_DAY));
            mCalendar.set(Calendar.MINUTE, nowCalendar.get(Calendar.MINUTE));
        }

        if (mDateText.getText().equals(getString(R.string.date_today))) {
            mCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
            mCalendar.set(Calendar.MONTH, nowCalendar.get(Calendar.MONTH));
            mCalendar.set(Calendar.DAY_OF_MONTH, nowCalendar.get(Calendar.DAY_OF_MONTH));
        }

        // Check if the number of times to show notification is empty
        if (mTimesEditText.getText().toString().isEmpty()) {
            mTimesEditText.setText("1");
        }

        mTimesToShow = Integer.parseInt(mTimesEditText.getText().toString());
        if (mRepeatType == ReminderConstants.DOES_NOT_REPEAT) {
            mTimesToShow = mTimesShown + 1;
        }

        // Check if selected date is before today's date
        if (DateAndTimeUtil.toLongDateAndTime(mCalendar) < DateAndTimeUtil.toLongDateAndTime(nowCalendar)) {
            Snackbar.make(mCoordinatorLayout, R.string.toast_past_date, Snackbar.LENGTH_SHORT).show();
            mImageWarningTime.setVisibility(View.VISIBLE);
            mImageWarningDate.setVisibility(View.VISIBLE);

            // Check if title is empty
        } else if (mTitleEditText.getText().toString().trim().isEmpty()) {
            Snackbar.make(mCoordinatorLayout, R.string.toast_title_empty, Snackbar.LENGTH_SHORT).show();
            AnimationUtil.shakeView(mTitleEditText, this);

            // Check if times to show notification is too low
        } else if (mTimesToShow <= mTimesShown && !mForeverSwitch.isChecked()) {
            Snackbar.make(mCoordinatorLayout, R.string.toast_higher_number, Snackbar.LENGTH_SHORT).show();
            mImageWarningShow.setVisibility(View.VISIBLE);
        } else {
            saveNotification();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_recurence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save:
                validateInput();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDateSet(com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int i_year = year;
        int i_month = monthOfYear+1;
        int i_day = dayOfMonth ;

        persianDate = new PersianDate(i_year, i_month, i_day);
        civilDate = DateConverter.persianToCivil(persianDate);

        Log.e("[P DAY]",""+utils.shape(utils.dateToString(civilDate)));

        Log.e("[P YEAR]", "" + i_year);
        Log.e("[P MONTH]", "" + i_month);
        Log.e("[P DAY]", "" + i_day);

        int civil_year = civilDate.getYear();
        int civil_month = civilDate.getMonth()-1;
        int civil_day = civilDate.getDayOfMonth();

        mCalendar.set(Calendar.YEAR, civil_year);
        mCalendar.set(Calendar.MONTH, civil_month);
        mCalendar.set(Calendar.DAY_OF_MONTH, civil_day);

        int a = mCalendar.get(Calendar.YEAR);
        int b = mCalendar.get(Calendar.MONTH)+1;
        int c = mCalendar.get(Calendar.DAY_OF_MONTH);

        civilDateintro = new CivilDate(a,b,c);
        persianDateintro = DateConverter.civilToPersian(civilDateintro);
        mDateText.setText(utils.shape(utils.dateToString(persianDateintro)));



        Log.e("[YEAR]", "" + civilDate.getYear());
        Log.e("[MONTH]",""+civilDate.getMonth());
        Log.e("[DAY]",""+civilDate.getDayOfMonth());
        Log.e("[COMPLITE]",""+DateAndTimeUtil.toStringReadableDate(mCalendar));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);


        mTimeText.setText(DateAndTimeUtil.toStringReadableTime(mCalendar, getApplicationContext()));
    }
}