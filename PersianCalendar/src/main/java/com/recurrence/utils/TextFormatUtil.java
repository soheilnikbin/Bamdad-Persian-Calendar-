package com.recurrence.utils;

import android.content.Context;

import com.byagowi.persiancalendar.R;


public class TextFormatUtil {

    public static String formatDaysOfWeekText(Context context, boolean[] daysOfWeek) {
        final String[] shortWeekDays = {"یکشنبه ها","دوشنبه ها","سه شنبه ها","چهارشنبه ها","پنجشنبه ها","جمعه ها","شنبه ها"};
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getString(R.string.repeats_on));
        stringBuilder.append(" ");
        for (int i = 0; i < daysOfWeek.length; i++) {
            if (daysOfWeek[i]) {
                stringBuilder.append(shortWeekDays[i]);
                stringBuilder.append(" - ");
            }
        }
        return stringBuilder.toString();
    }

    public static String formatAdvancedRepeatText(Context context, int repeatType, int interval) {
        String typeText;
        switch (repeatType) {
            default:
            case ReminderConstants.HOURLY:
                typeText = context.getResources().getQuantityString(R.plurals.hour, interval);
                break;
            case ReminderConstants.DAILY:
                typeText = context.getResources().getQuantityString(R.plurals.day, interval);
                break;
            case ReminderConstants.WEEKLY:
                typeText = context.getResources().getQuantityString(R.plurals.week, interval);
                break;
            case ReminderConstants.MONTHLY:
                typeText = context.getResources().getQuantityString(R.plurals.month, interval);
                break;
            case ReminderConstants.YEARLY:
                typeText = context.getResources().getQuantityString(R.plurals.year, interval);
                break;
        }
        return context.getString(R.string.repeats_every, interval, typeText);
    }
}