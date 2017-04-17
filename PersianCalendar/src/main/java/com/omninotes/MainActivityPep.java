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

package com.omninotes;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.Bind;
import butterknife.ButterKnife;

import calendar.Config;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import edu.emory.mathcs.backport.java.util.Arrays;

import com.afollestad.materialdialogs.MaterialDialog;
import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.adapter.DrawerAdapter;
import com.omninotes.async.bus.SwitchFragmentEvent;
import com.omninotes.async.notes.NoteProcessorDelete;
import com.omninotes.db.DbHelper;
import com.omninotes.models.Attachment;
import com.omninotes.models.Category;
import com.omninotes.models.Note;
import com.omninotes.utils.Constants;
import com.omninotes.utils.KeyboardUtils;
import com.omninotes.utils.MiscUtils;
import com.omninotes.utils.Security;
import com.prayer.App;
import com.recurrence.activities.MainActivity;


public class MainActivityPep extends BaseActivity implements OnDateSetListener, OnTimeSetListener {

    @Bind(R.id.crouton_handle) ViewGroup croutonViewContainer;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    public final String FRAGMENT_DRAWER_TAG = "fragment_drawer";
    public final String FRAGMENT_LIST_TAG = "fragment_list";
    public final String FRAGMENT_DETAIL_TAG = "fragment_detail";
    public final String FRAGMENT_SKETCH_TAG = "fragment_sketch";
    private FragmentManager mFragmentManager;
    public Uri sketchUri;
    public static RelativeLayout relativeLayout;
    // private AdView adView;
    MaterialDialog dialog;
    boolean noteFromRecurence = false;
    boolean newNoteFromMainActivity = false;
    boolean newNoteFromMainActivityList = false;
    boolean newNoteFromMainActivityCamera = false;
    SoftKeyboard softKeyboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.OmniNotesTheme_ApiSpec);
        setContentView(R.layout.activity_main_pep);
        try{
            noteFromRecurence = getIntent().getExtras().getBoolean("TabFragmentNotesBoolean");
            newNoteFromMainActivity = getIntent().getExtras().getBoolean("NewNoteFromMainActivity");
            newNoteFromMainActivityList = getIntent().getExtras().getBoolean("NewNoteFromMainActivityList");
            newNoteFromMainActivityCamera = getIntent().getExtras().getBoolean("NewNoteFromMainActivityCamera");
        }catch (Exception e){
        }
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M){
            insertDummyContactWrapper();
        }

        if (App.getContext() == null) {
            App.setContext(this);
        }

        relativeLayout = (RelativeLayout) findViewById(R.id.pep_main_view);

        ButterKnife.bind(this);

        // This method starts the bootstrap chain.
        checkPassword();

        initUI();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    private void checkPassword() {
        if (prefs.getString(Constants.PREF_PASSWORD, null) != null
                && prefs.getBoolean("settings_password_access", false)) {
            requestPassword(this, passwordConfirmed -> {
                if (passwordConfirmed) {
                    init();
                } else {
                    finish();
                }
            });
        } else {
            init();
        }
    }


    private void init() {
        mFragmentManager = getSupportFragmentManager();

        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment) mFragmentManager
                .findFragmentById(R.id.navigation_drawer);
        if (mNavigationDrawerFragment == null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.navigation_drawer, new NavigationDrawerFragment(),
                    FRAGMENT_DRAWER_TAG).commit();
        }

        if (mFragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG) == null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, new ListFragment(), FRAGMENT_LIST_TAG).commit();
        }

        // Handling of Intent actions
        handleIntents();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction() == null) {
            intent.setAction(Constants.ACTION_START_APP);
        }
        setIntent(intent);
        handleIntents();
        Log.d(Constants.TAG, "onNewIntent");
        super.onNewIntent(intent);
    }


    public MenuItem getSearchMenuItem() {
        Fragment f = checkFragmentInstance(R.id.fragment_container, ListFragment.class);
        if (f != null) {
            return ((ListFragment) f).getSearchMenuItem();
        } else {
            return null;
        }
    }


    public void editTag(Category tag) {
        Fragment f = checkFragmentInstance(R.id.fragment_container, ListFragment.class);
        if (f != null) {
            ((ListFragment) f).editCategory(tag);
        }
    }


    public void initNotesList(Intent intent) {
        Fragment f = checkFragmentInstance(R.id.fragment_container, ListFragment.class);
        if (f != null) {
            ((ListFragment) f).toggleSearchLabel(false);
            ((ListFragment) f).initNotesList(intent);
        }
    }


    public void commitPending() {
        Fragment f = checkFragmentInstance(R.id.fragment_container, ListFragment.class);
        if (f != null) {
            ((ListFragment) f).commitPending();
        }
    }


    /**
     * Checks if allocated fragment is of the required type and then returns it or returns null
     */
    private Fragment checkFragmentInstance(int id, Object instanceClass) {
        Fragment result = null;
        if (mFragmentManager != null) {
            Fragment fragment = mFragmentManager.findFragmentById(id);
            if (instanceClass.equals(fragment.getClass())) {
                result = fragment;
            }
        }
        return result;
    }


    /*
     * (non-Javadoc)
     * @see android.support.v7.app.ActionBarActivity#onBackPressed()
     *
     * Overrides the onBackPressed behavior for the attached fragments
     */
    public void onBackPressed() {

        if(noteFromRecurence == true ){
            startActivity(new Intent(MainActivityPep.this, MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return;
        }
        if(newNoteFromMainActivity == true ||
                newNoteFromMainActivityList == true ||
                newNoteFromMainActivityCamera == true){
            finish();
            return;
        }
        Fragment f;

        // SketchFragment
        f = checkFragmentInstance(R.id.fragment_container, SketchFragment.class);
        if (f != null) {
            ((SketchFragment) f).save();

            // Removes forced portrait orientation for this fragment
            setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            mFragmentManager.popBackStack();

            return;
        }

        // DetailFragment
        f = checkFragmentInstance(R.id.fragment_container, DetailFragment.class);
        if (f != null) {
            ((DetailFragment) f).goBack = true;
            ((DetailFragment) f).saveAndExit((DetailFragment) f);

            return;

        }

        // ListFragment
        f = checkFragmentInstance(R.id.fragment_container, ListFragment.class);
        if (f != null) {
            // Before exiting from app the navigation drawer is opened
            if (prefs.getBoolean("settings_navdrawer_on_exit", false) && getDrawerLayout() != null &&
                    !getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
                getDrawerLayout().openDrawer(GravityCompat.START);
            } else if (!prefs.getBoolean("settings_navdrawer_on_exit", false) && getDrawerLayout() != null &&
                    getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
                getDrawerLayout().closeDrawer(GravityCompat.START);
            } else {
                if (!((ListFragment)f).closeFab()) {
                    super.onBackPressed();
                }
            }

            return;
        }
        super.onBackPressed();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("navigationTmp", navigationTmp);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Crouton.cancelAllCroutons();

    }


    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }


    public ActionBarDrawerToggle getDrawerToggle() {
        if(newNoteFromMainActivity == true||
                newNoteFromMainActivityList == true ||
                newNoteFromMainActivityCamera == true){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
            finish();
            Log.e("FINISH NOTE","");
            return null;
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Config.animateMenu = true;
            }
        }, 550);

        if (mFragmentManager != null && mFragmentManager.findFragmentById(R.id.navigation_drawer) != null) {
            if(noteFromRecurence == true){
                startActivity(new Intent(MainActivityPep.this, MainActivity.class));
                finish();
            }
            return ((NavigationDrawerFragment) mFragmentManager.findFragmentById(R.id.navigation_drawer)).mDrawerToggle;
        } else {
            return null;
        }
    }


    /**
     * Finishes multiselection mode started by ListFragment
     */
    public void finishActionMode() {
        ListFragment fragment = (ListFragment) mFragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG);
        if (fragment != null) {
            fragment.finishActionMode();
        }
    }


    Toolbar getToolbar() {
        return this.toolbar;
    }


    private void handleIntents() {
        Intent i = getIntent();

        if (i.getAction() == null) return;

        if (Constants.ACTION_RESTART_APP.equals(i.getAction())) {
            MiscUtils.restartApp(getApplicationContext(), MainActivityPep.class);
        }

        if (receivedIntent(i)) {
            Note note = i.getParcelableExtra(Constants.INTENT_NOTE);
            if (note == null) {
                note = DbHelper.getInstance().getNote(i.getIntExtra(Constants.INTENT_KEY, 0));
            }
            // Checks if the same note is already opened to avoid to open again
            if (note != null && noteAlreadyOpened(note)) {
                return;
            }
            // Empty note instantiation
            if (note == null) {
                note = new Note();
            }
            switchToDetail(note);
        }

        if (Constants.ACTION_SEND_AND_EXIT.equals(i.getAction())) {
            saveAndExit(i);
        }

        // Tag search
        if (Intent.ACTION_VIEW.equals(i.getAction())) {
            switchToList();
        }
    }


    /**
     * Used to perform a quick text-only note saving (eg. Tasker+Pushbullet)
     */
    private void saveAndExit(Intent i) {
        Note note = new Note();
        note.setTitle(i.getStringExtra(Intent.EXTRA_SUBJECT));
        note.setContent(i.getStringExtra(Intent.EXTRA_TEXT));
        DbHelper.getInstance().updateNote(note, true);
        showToast(getString(R.string.note_updated), Toast.LENGTH_SHORT);
        finish();

    }


    private boolean receivedIntent(Intent i) {
        return Constants.ACTION_SHORTCUT.equals(i.getAction())
                || Constants.ACTION_NOTIFICATION_CLICK.equals(i.getAction())
                || Constants.ACTION_WIDGET.equals(i.getAction())
                || Constants.ACTION_TAKE_PHOTO.equals(i.getAction())
                || ((Intent.ACTION_SEND.equals(i.getAction())
                || Intent.ACTION_SEND_MULTIPLE.equals(i.getAction())
                || Constants.INTENT_GOOGLE_NOW.equals(i.getAction()))
                && i.getType() != null)
                || i.getAction().contains(Constants.ACTION_NOTIFICATION_CLICK);
    }


    private boolean noteAlreadyOpened(Note note) {
        DetailFragment detailFragment = (DetailFragment) mFragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG);
        return detailFragment != null && detailFragment.getCurrentNote() != null && detailFragment.getCurrentNote()
                .get_id() == note.get_id();
    }


    public void switchToList() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        animateTransition(transaction, TRANSITION_HORIZONTAL);
        ListFragment mListFragment = new ListFragment();
        transaction.replace(R.id.fragment_container, mListFragment, FRAGMENT_LIST_TAG).addToBackStack
                (FRAGMENT_DETAIL_TAG).commitAllowingStateLoss();
        if (getDrawerToggle() != null) {
            getDrawerToggle().setDrawerIndicatorEnabled(false);

        }
        mFragmentManager.getFragments();
        EventBus.getDefault().post(new SwitchFragmentEvent(SwitchFragmentEvent.Direction.PARENT));
    }


    public void switchToDetail(Note note) {
        Config.animateMenu = false;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        animateTransition(transaction, TRANSITION_HORIZONTAL);
        DetailFragment mDetailFragment = new DetailFragment();
        Bundle b = new Bundle();
        b.putParcelable(Constants.INTENT_NOTE, note);
        mDetailFragment.setArguments(b);
        if (mFragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG) == null) {
            transaction.replace(R.id.fragment_container, mDetailFragment, FRAGMENT_DETAIL_TAG).addToBackStack
                    (FRAGMENT_LIST_TAG).commitAllowingStateLoss();
        } else {
            transaction.replace(R.id.fragment_container, mDetailFragment, FRAGMENT_DETAIL_TAG).addToBackStack
                    (FRAGMENT_DETAIL_TAG).commitAllowingStateLoss();
        }
    }


    /**
     * Notes sharing
     */
    public void shareNote(Note note) {

        String titleText = note.getTitle();

        String contentText = titleText
                + System.getProperty("line.separator")
                + note.getContent();


        Intent shareIntent = new Intent();
        // Prepare sharing intent with only text
        if (note.getAttachmentsList().size() == 0) {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, titleText);
            shareIntent.putExtra(Intent.EXTRA_TEXT, contentText);

            // Intent with single image attachment
        } else if (note.getAttachmentsList().size() == 1) {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType(note.getAttachmentsList().get(0).getMime_type());
            shareIntent.putExtra(Intent.EXTRA_STREAM, note.getAttachmentsList().get(0).getUri());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, titleText);
            shareIntent.putExtra(Intent.EXTRA_TEXT, contentText);

            // Intent with multiple images
        } else if (note.getAttachmentsList().size() > 1) {
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            ArrayList<Uri> uris = new ArrayList<>();
            // A check to decide the mime type of attachments to share is done here
            HashMap<String, Boolean> mimeTypes = new HashMap<>();
            for (Attachment attachment : note.getAttachmentsList()) {
                uris.add(attachment.getUri());
                mimeTypes.put(attachment.getMime_type(), true);
            }
            // If many mime types are present a general type is assigned to intent
            if (mimeTypes.size() > 1) {
                shareIntent.setType("*/*");
            } else {
                shareIntent.setType((String) mimeTypes.keySet().toArray()[0]);
            }

            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, titleText);
            shareIntent.putExtra(Intent.EXTRA_TEXT, contentText);
        }

        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_message_chooser)));
    }


    /**
     * Single note permanent deletion
     *
     * @param note Note to be deleted
     */
    public void deleteNote(Note note) {
        new NoteProcessorDelete(Arrays.asList(new Note[]{note})).process();
        BaseActivity.notifyAppWidgets(this);
        Log.d(Constants.TAG, "Deleted permanently note with id '" + note.get_id() + "'");

    }


    public void showMessage(int messageId, Style style) {
        showMessage(getString(messageId), style);
    }


    public void showMessage(String message, Style style) {
        // ViewGroup used to show Crouton keeping compatibility with the new Toolbar
        Crouton.makeText(this, message, style, croutonViewContainer).show();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DetailFragment f = (DetailFragment) mFragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG);
        if (f != null && f.isAdded()) {
            f.onTimeSetListener.onTimeSet(view, hourOfDay, minute);
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        DetailFragment f = (DetailFragment) mFragmentManager.findFragmentByTag(FRAGMENT_DETAIL_TAG);
        if (f != null && f.isAdded()) {
            f.onDateSetListener.onDateSet(view, year, monthOfYear, dayOfMonth);
        }

    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @TargetApi(Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Contacts");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                dialog = new MaterialDialog.Builder(this)
                        .autoDismiss(false)
                        .cancelable(false)
                        .title("دسترسی\u200Cهای مورد نیاز")
                        .content("Location : موقعیت جغرافیایی جهت ثبت آن در یادداشت\u200Cهای شما در صورت نیاز"+"\n\n"
                                +"Microphone : جهت ضبط صدا در قسمت یادداشت ها "+"\n\n"
                                +"Storage : دسترسی به حافظه داخلی\u200C گوشی جهت خروجی گرفتن از یادداشت\u200Cها و یا بازگردانی آنها از حافظه داخلی\u200C. ")
                        .positiveText("روشن کردن دسترسی ها")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                dialog.dismiss();
                            }
                        }).build();
                dialog.show();
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }


    }


    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(this,"some permission not accepted",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
