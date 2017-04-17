package com.recurrence.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.recurrence.database.DatabaseHelper;
import com.recurrence.models.Reminder;
import com.recurrence.utils.NotificationUtil;

public class NagReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper database = DatabaseHelper.getInstance(context);
        int reminderId = intent.getIntExtra("NOTIFICATION_ID", 0);
        if (reminderId != 0 && database.isNotificationPresent(reminderId)) {
            Reminder reminder = database.getNotification(reminderId);
            NotificationUtil.createNotification(context, reminder);
        }
        database.close();
    }
}