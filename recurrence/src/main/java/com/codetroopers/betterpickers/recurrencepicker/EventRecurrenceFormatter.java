/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.util.TimeFormatException;
import android.widget.Switch;

import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.calendar.AbstractDate;
import com.codetroopers.betterpickers.calendar.CivilDate;
import com.codetroopers.betterpickers.calendar.DateConverter;
import com.codetroopers.betterpickers.calendar.PersianDate;

import java.util.Calendar;

public class EventRecurrenceFormatter {

    private static int[] mMonthRepeatByDayOfWeekIds;
    private static String[][] mMonthRepeatByDayOfWeekStrs;
    private static CivilDate civilDateintro;
    public static String getRepeatString(Context context, Resources r, EventRecurrence recurrence, boolean includeEndString) {
        String endString = "";
        if (includeEndString) {
            StringBuilder sb = new StringBuilder();
            if (recurrence.until != null) {
                try {
                    Time time = new Time();
                    time.parse(recurrence.until);

                    Calendar c = Calendar.getInstance();
                    int currentHour = c.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = c.get(Calendar.MINUTE);

                    CivilDate currentCivilDateintro = new CivilDate(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                    PersianDate persianDateintro = DateConverter.civilToPersian(currentCivilDateintro);

                    int SPM = persianDateintro.getMonth();
                    int surrentPersianMounth = SPM + 1 ;

                    switch (surrentPersianMounth){
                        case 1:
                            if(currentHour >= 0 && currentHour <= 4 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 2:
                            if(currentHour >= 0 && currentHour <= 4 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 3:
                            if(currentHour >= 0 && currentHour <= 4 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 4:
                            if(currentHour >= 0 && currentHour <= 4 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 5:
                            if(currentHour >= 0 && currentHour <= 4 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 6:
                            if(currentHour >= 0 && currentHour <= 3 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 7:
                            if(currentHour >= 0 && currentHour <= 3 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 8:
                            if(currentHour >= 0 && currentHour <= 3 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 9:
                            if(currentHour >= 0 && currentHour <= 3 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 10:
                            if(currentHour >= 0 && currentHour <= 3 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 11:
                            if(currentHour >= 0 && currentHour <= 3 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        case 12:
                            if(currentHour >= 0 && currentHour <= 3 && currentMinute >= 0 && currentMinute <= 29 ){
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay + 1);
                            }
                            else{
                                civilDateintro = new CivilDate(time.year
                                        ,time.month + 1
                                        ,time.monthDay);
                            }
                            break;
                        default:
                            civilDateintro = new CivilDate(time.year
                                    ,time.month + 1
                                    ,time.monthDay);
                            break;
                    }

                    int tt = time.monthDay;
                    Log.e("[CIVIL YEAR]", "" + time.year);
                    Log.e("[CIVIL MONTH]","" + time.month);
                    Log.e("[CIVIL DAY]","" + tt);


                    PersianDate persianDate = DateConverter.civilToPersian(civilDateintro);
                    String year = ConvertPersianCal.shape(ConvertPersianCal.dateToString_getYear(persianDate));
                    String mounth = ConvertPersianCal.shape(ConvertPersianCal.dateToString_getMounth(persianDate));
                    String day = ConvertPersianCal.shape(ConvertPersianCal.dateToString_getDay(persianDate));
                    String stringMounth = "";
                    switch (Integer.parseInt(mounth)){
                        case 1:
                            stringMounth = "فروردین";
                            break;
                        case 2:
                            stringMounth = "اردیبهشت";
                            break;
                        case 3:
                            stringMounth = "خرداد";
                            break;
                        case 4:
                            stringMounth = "تیر";
                            break;
                        case 5:
                            stringMounth = "مرداد";
                            break;
                        case 6:
                            stringMounth = "شهریور";
                            break;
                        case 7:
                            stringMounth = "مهر";
                            break;
                        case 8:
                            stringMounth = "آبان";
                            break;
                        case 9:
                            stringMounth = "آذر";
                            break;
                        case 10:
                            stringMounth = "دی";
                            break;
                        case 11:
                            stringMounth = "بهمن";
                            break;
                        case 12:
                            stringMounth = "اسفند";
                            break;
                        default:
                            stringMounth = "";
                            break;

                    }

                    sb.append(r.getString(R.string.endByDate,
                            ConvertPersianCal.toPersianNumber(day)
                            +" "+stringMounth+" "
                            +ConvertPersianCal.toPersianNumber(year)+" - "));
                } catch (TimeFormatException e) {
                }
            }

            if (recurrence.count > 0) {
                sb.append(r.getQuantityString(R.plurals.endByCount, recurrence.count,
                        recurrence.count));
            }
            endString = sb.toString();
        }

        // TODO Implement "Until" portion of string, as well as custom settings
        int interval = recurrence.interval <= 1 ? 1 : recurrence.interval;
        switch (recurrence.freq) {
            case EventRecurrence.HOURLY:
                return r.getQuantityString(R.plurals.hourly, interval, interval) + endString;
            case EventRecurrence.DAILY:
                return r.getQuantityString(R.plurals.daily, interval, interval) + endString;
            case EventRecurrence.WEEKLY: {
                if (recurrence.repeatsOnEveryWeekDay()) {
                    return r.getString(R.string.every_weekday) + endString;
                } else {
                    String string;

                    int dayOfWeekLength = DateUtils.LENGTH_MEDIUM;
                    if (recurrence.bydayCount == 1) {
                        dayOfWeekLength = DateUtils.LENGTH_LONG;
                    }

                    StringBuilder days = new StringBuilder();

                    // Do one less iteration in the loop so the last element is added out of the
                    // loop. This is done so the comma is not placed after the last item.

                    if (recurrence.bydayCount > 0) {
                        int count = recurrence.bydayCount - 1;
                        Log.e("bydayCount"," "+recurrence.bydayCount);
                        for (int i = 0; i < count; i++) {
                            switch (dayToString(recurrence.byday[i], dayOfWeekLength)){
                                case "Sun":
                                    days.append("یکشنبه");
                                    break;
                                case "Mon":
                                    days.append("دوشنبه");
                                    break;
                                case "Tue":
                                    days.append("سه شنبه");
                                    break;
                                case "Wed":
                                    days.append("چهار شنبه");
                                    break;
                                case "Thu":
                                    days.append("پنج شنبه");
                                    break;
                                case "Fri":
                                    days.append("جمعه");
                                    break;
                                case "Sat":
                                    days.append("شنبه");
                                    break;
                                default:
                                    days.append(dayToString(recurrence.byday[i], dayOfWeekLength));
                                    break;
                            }
                            days.append(", ");
                        }
                        switch (dayToString(recurrence.byday[count], dayOfWeekLength)) {
                            case "Sun":
                                days.append("یکشنبه");
                                break;
                            case "Mon":
                                days.append("دوشنبه");
                                break;
                            case "Tue":
                                days.append("سه شنبه");
                                break;
                            case "Wed":
                                days.append("چهار شنبه");
                                break;
                            case "Thu":
                                days.append("پنج شنبه");
                                break;
                            case "Fri":
                                days.append("جمعه");
                                break;
                            case "Sat":
                                days.append("شنبه");
                                break;
                            default:
                                days.append(dayToString(recurrence.byday[count], dayOfWeekLength));
                                break;
                        }
                        string = days.toString();
                    } else {
                        // There is no "BYDAY" specifier, so use the day of the
                        // first event.  For this to work, the setStartDate()
                        // method must have been used by the caller to set the
                        // date of the first event in the recurrence.
                        if (recurrence.startDate == null) {
                            return null;
                        }

                        int day = EventRecurrence.timeDay2Day(recurrence.startDate.weekDay);
                        string = dayToString(day, DateUtils.LENGTH_LONG);
                    }
                    return r.getQuantityString(R.plurals.weekly, interval, interval, string) + endString;
                }
            }
            case EventRecurrence.MONTHLY: {
                String details = "";
                if (recurrence.byday != null) {
                    int weekday = EventRecurrence.day2CalendarDay(recurrence.byday[0]) - 1;
                    // Cache this stuff so we won't have to redo work again later.
                    cacheMonthRepeatStrings(r, weekday);

                    int dayNumber = recurrence.bydayNum[0];
                    if(dayNumber == RecurrencePickerDialogFragment.LAST_NTH_DAY_OF_WEEK){
                        dayNumber = 5;
                    }
                    details = mMonthRepeatByDayOfWeekStrs[weekday][dayNumber - 1];
                }
                return r.getQuantityString(R.plurals.monthly, interval, interval, details) + endString;
            }
            case EventRecurrence.YEARLY:
                return r.getQuantityString(R.plurals.yearly_plain, interval, interval, "") + endString;
        }

        return null;
    }

    private static void cacheMonthRepeatStrings(Resources r, int weekday) {
        if (mMonthRepeatByDayOfWeekIds == null) {
            mMonthRepeatByDayOfWeekIds = new int[7];
            mMonthRepeatByDayOfWeekIds[0] = R.array.repeat_by_nth_sun;
            mMonthRepeatByDayOfWeekIds[1] = R.array.repeat_by_nth_mon;
            mMonthRepeatByDayOfWeekIds[2] = R.array.repeat_by_nth_tues;
            mMonthRepeatByDayOfWeekIds[3] = R.array.repeat_by_nth_wed;
            mMonthRepeatByDayOfWeekIds[4] = R.array.repeat_by_nth_thurs;
            mMonthRepeatByDayOfWeekIds[5] = R.array.repeat_by_nth_fri;
            mMonthRepeatByDayOfWeekIds[6] = R.array.repeat_by_nth_sat;
        }
        if (mMonthRepeatByDayOfWeekStrs == null) {
            mMonthRepeatByDayOfWeekStrs = new String[7][];
        }
        if (mMonthRepeatByDayOfWeekStrs[weekday] == null) {
            mMonthRepeatByDayOfWeekStrs[weekday] = r.getStringArray(mMonthRepeatByDayOfWeekIds[weekday]);
        }
    }

    /**
     * Converts day of week to a String.
     *
     * @param day a EventRecurrence constant
     * @return day of week as a string
     */
    private static String dayToString(int day, int dayOfWeekLength) {
        return DateUtils.getDayOfWeekString(dayToUtilDay(day), dayOfWeekLength);
    }

    /**
     * Converts EventRecurrence's day of week to DateUtil's day of week.
     *
     * @param day of week as an EventRecurrence value
     * @return day of week as a DateUtil value.
     */
    private static int dayToUtilDay(int day) {
        switch (day) {
            case EventRecurrence.SU:
                return Calendar.SUNDAY;
            case EventRecurrence.MO:
                return Calendar.MONDAY;
            case EventRecurrence.TU:
                return Calendar.TUESDAY;
            case EventRecurrence.WE:
                return Calendar.WEDNESDAY;
            case EventRecurrence.TH:
                return Calendar.THURSDAY;
            case EventRecurrence.FR:
                return Calendar.FRIDAY;
            case EventRecurrence.SA:
                return Calendar.SATURDAY;
            default:
                throw new IllegalArgumentException("bad day argument: " + day);
        }
    }
}
