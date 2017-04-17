package com.omninotes.async.bus;

import android.util.Log;

import com.omninotes.models.Note;
import com.omninotes.utils.Constants;

import java.util.ArrayList;





public class NotesLoadedEvent {

	public ArrayList<Note> notes;


	public NotesLoadedEvent(ArrayList<Note> notes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.notes = notes;
	}
}
