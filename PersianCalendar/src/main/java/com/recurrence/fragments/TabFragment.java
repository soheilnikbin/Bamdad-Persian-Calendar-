package com.recurrence.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.byagowi.persiancalendar.R;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.omninotes.MainActivityPep;
import com.omninotes.async.notes.NoteLoaderTask;
import com.omninotes.db.DbHelper;
import com.omninotes.models.Note;
import com.omninotes.receiver.AlarmReceiver;
import com.omninotes.utils.Constants;
import com.omninotes.utils.Display;
import com.omninotes.utils.TextHelper;
import com.omninotes.utils.date.DateHelper;
import com.recurrence.activities.MainActivity;
import com.recurrence.adapters.NotesAdapter;
import com.recurrence.adapters.ReminderAdapter;
import com.recurrence.adapters.ReminderNotesAdapter;
import com.recurrence.database.DatabaseHelper;
import com.recurrence.models.Reminder;
import com.recurrence.utils.ReminderConstants;

import java.util.ArrayList;
import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;
import it.feio.android.pixlui.links.UrlCompleter;

public class TabFragment extends Fragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.recycler_view_note)
    RecyclerView mRecyclerViewNotes;
    @Bind(R.id.empty_text) TextView mEmptyText;
    @Bind(R.id.empty_view) LinearLayout mLinearLayout;
    @Bind(R.id.empty_view_nots) LinearLayout mLinearLayout_nots;
    @Bind(R.id.empty_icon) ImageView mImageView;
    @Bind(R.id.fragment_container_note) FrameLayout noteLayout;
    @Bind(R.id.list_note) DynamicListView list;
    private Activity mActivity;
    private static Activity staticActivity;
    private ReminderAdapter mReminderAdapter;
    private List<Reminder> mReminderList;
    private int mRemindersType;
    private com.neopixl.pixlui.components.textview.TextView listFooter;
    int color_of_note ;
    MainActivity mainActivity;
    private List<Note> notes = new ArrayList<>();

    private List<ReminderNotesAdapter> notesList = new ArrayList<>();
    private NotesAdapter mAdapter;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs_recurence, container, false);
        mActivity = getActivity();
        staticActivity = getActivity();

        DbHelper dbHelper = DbHelper.getInstance();
        notes = dbHelper.getAllReminderNotes();

        for (int i = 0 ; i < notes.size() ; i++){

            Note note = notes.get(i);
            TextHelper.parseTitleAndContent(mActivity, note);
            Spanned[] titleAndContent = TextHelper.parseTitleAndContent(mActivity, note);
            String dateText = TextHelper.getDateText(mActivity, note, i);

            note.getAlarm();
            String reminder = initReminder(note);

            try{
                color_of_note = Integer.parseInt(note.getCategory().getColor());
            }catch (Exception e){
                color_of_note = getResources().getColor(R.color.default_colour_grey);
            }

            ReminderNotesAdapter ListNotesAdapter = new ReminderNotesAdapter(
                    titleAndContent[0].toString(),
                    titleAndContent[1].toString(),
                    dateText,
                    reminder,
                    color_of_note,
                    note.get_id());
            notesList.add(ListNotesAdapter);


        }


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);

        mRemindersType = this.getArguments().getInt("TYPE");
        if (mRemindersType == ReminderConstants.INACTIVE) {
            mEmptyText.setText(R.string.no_inactive);
            mImageView.setImageResource(R.drawable.ic_notifications_off_black_empty);
        }

        mReminderList = getListData();
        mReminderAdapter = new ReminderAdapter(mActivity, R.layout.item_notification_list_recurence, mReminderList);
        if(mRemindersType == ReminderConstants.ACTIVE ||mRemindersType == ReminderConstants.INACTIVE){
            mRecyclerView.setAdapter(mReminderAdapter);

            noteLayout.setVisibility(View.GONE);


        }

        if(mRemindersType == ReminderConstants.NOTE){

            mAdapter = new NotesAdapter(notesList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerViewNotes.setLayoutManager(mLayoutManager);
            mRecyclerViewNotes.setItemAnimator(new DefaultItemAnimator());
            mRecyclerViewNotes.setAdapter(mAdapter);

            mRecyclerViewNotes.addOnItemTouchListener(new RecyclerTouchListener(mActivity, mRecyclerViewNotes, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    ReminderNotesAdapter notes_id = notesList.get(position);
                    Intent intent = new Intent(mActivity, MainActivityPep.class);
                    intent.putExtra(Constants.INTENT_KEY, notes_id.getNoteID());
                    intent.putExtra("TabFragmentNotesBoolean",true);
                    intent.setAction(Constants.ACTION_SHORTCUT);
                    startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    mActivity.finish();
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }
        if (mReminderAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public List<Reminder> getListData() {
        DatabaseHelper database = DatabaseHelper.getInstance(mActivity.getApplicationContext());
        List<Reminder> reminderList = database.getNotificationList(mRemindersType);
        database.close();
        return reminderList;
    }

    public void updateList() {
        mReminderList.clear();
        mReminderList.addAll(getListData());
        mReminderAdapter.notifyDataSetChanged();


        if(mRemindersType == ReminderConstants.NOTE){
            mRecyclerViewNotes.setVisibility(View.VISIBLE);
            mLinearLayout_nots.setVisibility(View.GONE);
        }
        if(notesList.size() == 0 && mRemindersType == ReminderConstants.NOTE){
            mLinearLayout_nots.setVisibility(View.VISIBLE);
        }
        if (mReminderAdapter.getItemCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateList();
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(messageReceiver, new IntentFilter("BROADCAST_REFRESH"));
        updateList();
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(messageReceiver);
        super.onPause();
    }

    private String initReminder(Note note) {
        long reminder = Long.parseLong(note.getAlarm());
        String rrule = note.getRecurrenceRule();
        if (!TextUtils.isEmpty(rrule)) {
            return DateHelper.getNoteRecurrentReminderText(reminder, rrule);
        } else {
            return DateHelper.getNoteReminderText(reminder);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TabFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TabFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}