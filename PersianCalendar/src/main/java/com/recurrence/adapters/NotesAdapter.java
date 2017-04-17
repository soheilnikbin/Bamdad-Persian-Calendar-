package com.recurrence.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.byagowi.persiancalendar.R;

import java.util.List;

/**
 * Created by vMagnify on 6/1/16.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private List<ReminderNotesAdapter> reminderlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, discription, creationDate, reminderDate;
        ImageView noteColor;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.reminder_note_title);
            discription = (TextView) view.findViewById(R.id.note_discription);
            creationDate = (TextView) view.findViewById(R.id.creation_date);
            reminderDate = (TextView) view.findViewById(R.id.note_reminder_date);
            noteColor = (ImageView) view.findViewById(R.id.notification_circle_note);
        }
    }


    public NotesAdapter(List<ReminderNotesAdapter> moviesList) {
        this.reminderlist = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String shortDiscription = null;
        String shortTitle = null;

        ReminderNotesAdapter reminderNotesAdapter = reminderlist.get(position);


        if(reminderNotesAdapter.getTitle().length() > 15){
            shortTitle= reminderNotesAdapter.getTitle().substring(0, 15);
            holder.title.setText(shortTitle + "...");
        }else{
            holder.title.setText(reminderNotesAdapter.getTitle());
        }

        if(reminderNotesAdapter.getDiscription().length() > 70){
            shortDiscription= reminderNotesAdapter.getDiscription().substring(0, 70);
            holder.discription.setText(shortDiscription + "...");
        }else{
            holder.discription.setText(reminderNotesAdapter.getDiscription());
        }

        try{
            holder.noteColor.setColorFilter(reminderNotesAdapter.getNoteColor());
        }catch (Exception e){

        }
        holder.creationDate.setText(reminderNotesAdapter.getCreationDate());
        holder.reminderDate.setText(reminderNotesAdapter.getReminderDate());
    }

    @Override
    public int getItemCount() {
        return reminderlist.size();
    }
}
