package com.omninotes.async.bus;

import android.util.Log;

import com.omninotes.utils.Constants;


public class NotesMergeEvent {

	public final boolean keepMergedNotes;


	public NotesMergeEvent(boolean keepMergedNotes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.keepMergedNotes = keepMergedNotes;
	}
}
