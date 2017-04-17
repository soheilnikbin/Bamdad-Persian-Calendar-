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
package com.omninotes.async.notes;

import android.os.AsyncTask;
import android.util.Log;

import com.omninotes.async.bus.NotesLoadedEvent;
import com.omninotes.db.DbHelper;
import com.omninotes.models.Note;
import com.omninotes.utils.Constants;

import java.lang.reflect.Method;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class NoteLoaderTask extends AsyncTask<Object, Void, ArrayList<Note>> {


	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<Note> doInBackground(Object... params) {
		ArrayList<Note> notes = new ArrayList<>();
		String methodName = params[0].toString();
		Object methodArgs = params[1];
		DbHelper db = DbHelper.getInstance();

		// If null argument an empty list will be returned
		if (methodArgs == null) {
			return notes;
		}

		// Checks the argument class with reflection
		@SuppressWarnings("rawtypes")
		Class[] paramClass = new Class[1];
		if (Boolean.class.isAssignableFrom(methodArgs.getClass())) {
			paramClass[0] = Boolean.class;
		} else {
			paramClass[0] = String.class;
		}

		// Retrieves and calls the right method
		Method method;
		try {
			method = db.getClass().getDeclaredMethod(methodName, paramClass);
			notes = (ArrayList<Note>) method.invoke(db,
					paramClass[0].cast(methodArgs));
		} catch (Exception e) {
			Log.e(Constants.TAG, "Error retrieving notes", e);
		}
		return notes;
	}

	@Override
	protected void onPostExecute(ArrayList<Note> notes) {
		super.onPostExecute(notes);
		EventBus.getDefault().post(new NotesLoadedEvent(notes));
	}
}
