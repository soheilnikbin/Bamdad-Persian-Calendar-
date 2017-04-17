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

package com.omninotes.utils.date;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.byagowi.persiancalendar.util.Utils;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrenceFormatter;
import com.codetroopers.betterpickers.recurrencepicker.RecurrencePickerDialogFragment;
import com.codetroopers.betterpickers.timezonepicker.TimeZoneInfo;
import com.codetroopers.betterpickers.timezonepicker.TimeZonePickerDialogFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.recurrence.utils.DateAndTimeUtil;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import calendar.CivilDate;
import calendar.DateConverter;
import calendar.PersianDate;

import com.omninotes.models.listeners.OnReminderPickedListener;
import com.omninotes.utils.Constants;



public class ReminderPickers implements OnDateSetListener,
        OnTimeSetListener,
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.OnDateSetListener,
        com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, RecurrencePickerDialogFragment.OnRecurrenceSetListener {
    public static final int TYPE_GOOGLE = 0;
    public static final int TYPE_AOSP = 1;

    private static final String TIMEPICKER = "TimePickerDialog",
            DATEPICKER = "DatePickerDialog";
    private Calendar mCalendar;
    private Utils utils;

    private String mRrule;
    private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";

    CivilDate civilDate = null;
    CivilDate civilDateintro = null;

    PersianDate persianDate, persianDateintro;

    private FragmentActivity mActivity;
    private OnReminderPickedListener mOnReminderPickedListener;
    private EventRecurrence mEventRecurrence = new EventRecurrence();

    private int pickerType;

    private int reminderYear;
    private int reminderMonth;
    private int reminderDay;
    private int hourOfDay;
    private int minutes;

    private boolean timePickerCalledAlready = false;
    private boolean recurrencePickerCalledAlready = false;
    private long presetDateTime;
    private String recurrenceRule;

                public ReminderPickers(FragmentActivity mActivity,
                           OnReminderPickedListener mOnReminderPickedListener, int pickerType) {
        this.mActivity = mActivity;
        this.mOnReminderPickedListener = mOnReminderPickedListener;
        this.pickerType = pickerType;
    }


    public void pick() {
        pick(null);
    }


    public void pick(Long presetDateTime) {
        pick(presetDateTime, "");
    }


    public void pick(Long presetDateTime, String recurrenceRule) {
        this.presetDateTime = DateHelper.getCalendar(presetDateTime).getTimeInMillis();
        this.recurrenceRule = recurrenceRule;
            showDateTimeSelectors();
    }


    /**
     * Show date and time pickers
     */
    protected void showDateTimeSelectors() {

        mCalendar = Calendar.getInstance();
        utils = Utils.getInstance(mActivity);
        // Sets actual time or previously saved in note
        PersianCalendar now = new PersianCalendar();
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog dpd = com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.newInstance(
                ReminderPickers.this,
                now.getPersianYear(),
                now.getPersianMonth(),
                now.getPersianDay());
        dpd.setThemeDark(true);

        dpd.show(mActivity.getFragmentManager(), DATEPICKER);
    }


    /**
     * Shows fallback date and time pickers for smaller screens
     */

    private void showRecurrencePickerDialog(String recurrenceRule) {

        FragmentManager fm = mActivity.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        Time time = new Time();
        time.setToNow();
        bundle.putLong(RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);

        // may be more efficient to serialize and pass in EventRecurrence
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, mRrule);

        RecurrencePickerDialogFragment rpd = (RecurrencePickerDialogFragment) fm.findFragmentByTag(
                FRAG_TAG_RECUR_PICKER);
        if (rpd != null) {
            rpd.dismiss();
        }
        rpd = new RecurrencePickerDialogFragment();
        rpd.setArguments(bundle);
        rpd.setOnRecurrenceSetListener(ReminderPickers.this);
        rpd.show(fm, FRAG_TAG_RECUR_PICKER);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        reminderYear = year;
        reminderMonth = monthOfYear;
        reminderDay = dayOfMonth;
        if (!timePickerCalledAlready) {    // Used to avoid native bug that calls onPositiveButtonPressed in the onClose()
            timePickerCalledAlready = true;
//            showTimePickerDialog(presetDateTime);
        }
    }


    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minutes = minute;
        if (!recurrencePickerCalledAlready) {    // Used to avoid native bug that calls onPositiveButtonPressed in the onClose()
            recurrencePickerCalledAlready = true;
            showRecurrencePickerDialog(recurrenceRule);
        }
    }


//    @Override
//    public void onRecurrenceSet(final String rrule) {
//        Calendar c = Calendar.getInstance();
//        c.set(reminderYear, reminderMonth, reminderDay, hourOfDay, minutes, 0);
//        if (mOnReminderPickedListener != null) {
//            mOnReminderPickedListener.onReminderPicked(c.getTimeInMillis());
//            mOnReminderPickedListener.onRecurrenceReminderPicked(rrule);
//        }
//    }

    @Override
    public void onDateSet(com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int i_year = year;
        int i_month = monthOfYear+1;
        int i_day = dayOfMonth ;

        persianDate = new PersianDate(i_year, i_month, i_day);
        civilDate = DateConverter.persianToCivil(persianDate);
        Calendar current = Calendar.getInstance();
        int currentDay = current.get(Calendar.DAY_OF_MONTH);
        int currentMonth = current.get(Calendar.MONTH);
        int currentYear = current.get(Calendar.YEAR);

        if(civilDate.getDayOfMonth() < currentDay && civilDate.getMonth() <= currentMonth + 1 && civilDate.getYear() <= currentYear){
            Toast.makeText(mActivity,"روزهای گذشته قابل یادآوری نیستند !",Toast.LENGTH_LONG).show();
        }else {

            int civil_year = civilDate.getYear();
            int civil_month = civilDate.getMonth() - 1;
            int civil_day = civilDate.getDayOfMonth();

            mCalendar.set(Calendar.YEAR, civil_year);
            mCalendar.set(Calendar.MONTH, civil_month);
            mCalendar.set(Calendar.DAY_OF_MONTH, civil_day);

            int a = mCalendar.get(Calendar.YEAR);
            int b = mCalendar.get(Calendar.MONTH) + 1;
            int c = mCalendar.get(Calendar.DAY_OF_MONTH);

            civilDateintro = new CivilDate(a, b, c);
            persianDateintro = DateConverter.civilToPersian(civilDateintro);

            Log.e("[YEAR]", "" + civilDate.getYear());
            Log.e("[MONTH]", "" + civilDate.getMonth());
            Log.e("[DAY]", "" + civilDate.getDayOfMonth());
            Log.e("[COMPLITE]", "" + DateAndTimeUtil.toStringReadableDate(mCalendar));

            reminderYear = civilDate.getYear();
            reminderMonth = civilDate.getMonth() - 1;
            reminderDay = civilDate.getDayOfMonth();

            PersianCalendar now = new PersianCalendar();
            com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog tpd = com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog.newInstance(
                    ReminderPickers.this,
                    now.get(PersianCalendar.HOUR_OF_DAY),
                    now.get(PersianCalendar.MINUTE),
                    true
            );
            tpd.setThemeDark(true);
            tpd.show(mActivity.getFragmentManager(), TIMEPICKER);
        }
    }

    @Override
    public void onTimeSet(com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout view, int hour, int minute) {

        hourOfDay = hour;
        minutes = minute;
        showRecurrencePickerDialog(recurrenceRule);
    }

    @Override
    public void onRecurrenceSet(String rrule) {
        Calendar c = Calendar.getInstance();
        c.set(reminderYear, reminderMonth, reminderDay, hourOfDay, minutes, 0);
        if (mOnReminderPickedListener != null) {
            mOnReminderPickedListener.onReminderPicked(c.getTimeInMillis());
            mOnReminderPickedListener.onRecurrenceReminderPicked(rrule);
        }
    }
}
