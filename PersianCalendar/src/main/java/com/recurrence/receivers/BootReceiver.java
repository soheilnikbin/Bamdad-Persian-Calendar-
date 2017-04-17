package com.recurrence.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.recurrence.database.DatabaseHelper;
import com.recurrence.models.Reminder;
import com.recurrence.utils.AlarmUtil;
import com.recurrence.utils.DateAndTimeUtil;
import com.recurrence.utils.ReminderConstants;

import java.util.Calendar;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper database = DatabaseHelper.getInstance(context);
        List<Reminder> reminderList = database.getNotificationList(ReminderConstants.ACTIVE);
        database.close();
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        for (Reminder reminder : reminderList) {
            Calendar calendar = DateAndTimeUtil.parseDateAndTime(reminder.getDateAndTime());
            calendar.set(Calendar.SECOND, 0);
            AlarmUtil.setAlarm(context, alarmIntent, reminder.getId(), calendar);
        }
    }
}