package com.recurrence.adapters;

/**
 * Created by vMagnify on 6/1/16.
 */
public class ReminderNotesAdapter {
    String Title, Discription, creationDate, reminderDate;
    int Color;
    long noteID;
     public ReminderNotesAdapter(
             String Title,
             String Discription,
             String creationDate,
             String reminderDate,
             int Color,
             long noteID){

         this.Title = Title;
         this.Discription = Discription;
         this.creationDate = creationDate;
         this.reminderDate = reminderDate;
         this.Color = Color;
         this.noteID = noteID;
}
    public String getTitle(){
        return Title;
    }
    public void setTitle(String string){
        this.Title = string;
    }

    public String getDiscription(){
        return Discription;
    }
    public void setDiscription(String string){
        this.Discription = string;
    }

    public String getCreationDate(){
        return creationDate;
    }
    public void setCreationDate(String string){
        this.creationDate = string;
    }

    public String getReminderDate(){
        return reminderDate;
    }
    public void setReminderDate(String string){
        this.reminderDate = string;
    }

    public int getNoteColor(){
        return Color;
    }
    public void setNoteColor(int string){
        this.Color = string;
    }

    public long getNoteID(){
        return noteID;
    }
    public void setNoteID(long string){
        this.noteID = string;
    }
}
