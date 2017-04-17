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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.byagowi.persiancalendar.view.activity.MainActivity;
import com.omninotes.BaseActivity;
import com.omninotes.OmniNotes;
import com.omninotes.db.DbHelper;
import com.omninotes.models.Note;
import com.omninotes.utils.Constants;
import com.omninotes.utils.ReminderHelper;

import java.util.List;




public class AlarmRestoreOnRebootService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Constants.TAG, "System rebooted: service refreshing reminders");
        Context mContext = getApplicationContext();

        BaseActivity.notifyAppWidgets(mContext);

        List<Note> notes = DbHelper.getInstance().getNotesWithReminderNotFired();
        Log.d(Constants.TAG, "Found " + notes.size() + " reminders");
        for (Note note : notes) {
            ReminderHelper.addReminder(OmniNotes.getAppContext(), note);
        }
        return Service.START_NOT_STICKY;
    }

}
