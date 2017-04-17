package com.omninotes.async.bus;

import android.util.Log;

import com.omninotes.utils.Constants;


/**
 * Created by fede on 18/04/15.
 */
public class NotesUpdatedEvent {

	public NotesUpdatedEvent() {
		Log.d(Constants.TAG, this.getClass().getName());
	}
}
