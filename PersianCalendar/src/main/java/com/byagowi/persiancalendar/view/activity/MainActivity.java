package com.byagowi.persiancalendar.view.activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.asha.ChromeLikeSwipeLayout;
import com.byagowi.persiancalendar.BuildConfig;
import com.byagowi.persiancalendar.Constants;
import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.adapter.DrawerAdapter;
import com.byagowi.persiancalendar.service.ApplicationService;
import com.byagowi.persiancalendar.util.UpdateUtils;
import com.byagowi.persiancalendar.util.Utils;
import com.byagowi.persiancalendar.view.fragment.AboutFragment;
import com.byagowi.persiancalendar.view.fragment.ApplicationPreferenceFragment;
import com.byagowi.persiancalendar.view.fragment.CalendarFragment;
import com.byagowi.persiancalendar.view.fragment.ConverterFragment;
import com.compass.compass.Main;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mypopsy.drawable.SearchArrowDrawable;
import com.omninotes.MainActivityPep;
import com.omninotes.OmniNotes;
import com.omninotes.helpers.AnalyticsHelper;
import com.prayer.App;
import com.prayer.vakit.MainStart;
import com.recurrence.activities.CreateEditActivity;
import com.recurrence.adapters.ReminderNotesAdapter;
import com.software.shell.fab.ActionButton;
import com.sothree.slidinguppanel.ScrollableViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Calendar;
import calendar.Config;
import calendar.ForecastConfig;
import cz.msebera.android.httpclient.Header;


/**
 * Program activity for android
 *
 * @author ebraminio
 */
public class MainActivity extends AppCompatActivity implements
        ActionMenuView.OnMenuItemClickListener{

    int section = 0;
    int searchON = 1;
    int searchOFF = 0;
    private Utils utils;
    private UpdateUtils updateUtils;

    private DrawerLayout drawerLayout;
    private DrawerAdapter adapter;

    static CoordinatorLayout mainLayout;
    private Class[] fragments = new Class[] {
            null,
            CalendarFragment.class,
            ConverterFragment.class,
            ApplicationPreferenceFragment.class,
            AboutFragment.class
    };
    AppBarLayout intro_layout;

    public static SlidingUpPanelLayout mLayout;

    private static final int CALENDAR = 1;
    private static final int TODO = 2;
    private static final int NOTE = 3;
    private static final int EXPENSE_MANAGER = 4;
    private static final int CONVERTER = 5;
    private static final int COMPASS = 6;
    private static final int PREFERENCE = 7;
    private static final int EXIT = 8;

    // Default selected fragment
    private static final int DEFAULT = CALENDAR;

    private int menuPosition = 0; // it should be zero otherwise #selectItem won't be called
    Drawable drawable = null;
    private String lastLocale;
    private String lastTheme;

    static TextView cityName, countryname, weatherText, windSpeed, humidity,
            pressure, visibility, lastUpdate, temperature, sunrise, sunset;
    public static Context context;
    static ImageView windDirection;
    static TextView ic1, ic2, ic3, ic4, ic5, ic6 ,ic7, ic8 , ic9, ic10;
    static TextView panelWeather, panelTemp, panelCity, weatherIcon,weather1, weather2, weather3, weather4, weather5 ,weather6, weather7, weather8, weather9, weather10
                    ,day1, day2, day3, day4, day5, day6, day7, day8, day9, day10
                    ,date1,date2, date3, date4, date5, date6, date7, date8, date9, date10
                    ,high1, high2 ,high3 ,high4 ,high5 ,high6 ,high7 ,high8 ,high9 ,high10
                    ,low1, low2 ,low3 ,low4 ,low5 ,low6 ,low7 ,low8 ,low9 ,low10;
    String CITY;
    String temp_inorder;
    SharedPreferences prefs;
    public static AppBarLayout APB;
    NestedScrollView scrollView;
    Typeface weatherFont;

    static SlidingUpPanelLayout slidingUpPanelLayout;
    RelativeLayout  fakeadd;
    public static ActionButton backToMainDay;
    ImageView upArrow, weatherUpdate;
    boolean isPanelOpen = true;
    boolean isPanelColse = true;
    static KProgressHUD progressBat;
    EditText searchContent;
    ChromeLikeSwipeLayout chromeLikeSwipeLayout;

    SharedPreferences firstRunMain;
    String value;
    int AppVersion;
    MaterialDialog updateAppDialog;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        utils = Utils.getInstance(getApplicationContext());
        utils.setTheme(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                 value = getIntent().getExtras().getString(key);
                Log.d("FIREBASE NOTIFICATION", "Key: " + key + " Value: " + value);
                // Write you own logic to implement Firbase
            }
        }
        utils.changeAppLanguage(this);
        utils.loadLanguageResource();
        lastLocale = utils.getAppLanguage();
        lastTheme = utils.getTheme();
        updateUtils = UpdateUtils.getInstance(getApplicationContext());
        context  = getBaseContext();
        if (App.getContext() == null) {
            App.setContext(this);
        }
        Config.init(context);
        if (!Utils.getInstance(this).isServiceRunning(ApplicationService.class)) {
            startService(new Intent(context, ApplicationService.class));
        }

        updateUtils.update(true);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSearchView();
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
        progressBat = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا صبر کنید")
                .setDetailsLabel("درحال دریافت اطلاعات ..")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        cityName = (TextView) findViewById(R.id.city_name);
        countryname = (TextView) findViewById(R.id.country_name);
        weatherIcon = (TextView) findViewById(R.id. weather_icon);
        weatherIcon.setTypeface(weatherFont);
        weatherText = (TextView) findViewById(R.id.weather_info);
        windSpeed = (TextView) findViewById(R.id.wind_speed);
        humidity = (TextView) findViewById(R.id.humidity);
        pressure = (TextView) findViewById(R.id.pressure);
        visibility = (TextView) findViewById(R.id.visibility);
        lastUpdate = (TextView) findViewById(R.id.last_update);
        temperature = (TextView) findViewById(R.id.temprature);
        sunrise = (TextView) findViewById(R.id.sunrise_text);
        sunset = (TextView) findViewById(R.id.sunset_text);
        windDirection = (ImageView) findViewById(R.id.wind_direction);
        APB = (AppBarLayout) findViewById(R.id.appbar);
        scrollView = (NestedScrollView) findViewById(R.id.login_form);
        mainLayout = (CoordinatorLayout) findViewById(R.id.app_main_layout);
        backToMainDay = (ActionButton) findViewById(R.id.fab);
        backToMainDay.setHideAnimation(ActionButton.Animations.NONE);
        backToMainDay.hide();
        upArrow = (ImageView) findViewById(R.id.uparrow);
        upArrow.setBackgroundResource(R.drawable.ic_uparrow);


        chromeLikeSwipeLayout = (ChromeLikeSwipeLayout) findViewById(R.id.chrome_like_swipe_layout);

        intro_layout = (AppBarLayout) findViewById(R.id.intro_bar);

        ic1 = (TextView) findViewById(R.id.forecast_icon_one);
        ic1.setTypeface(weatherFont);
        ic2 = (TextView) findViewById(R.id.forecast_icon_two);
        ic2.setTypeface(weatherFont);
        ic3 = (TextView) findViewById(R.id.forecast_icon_three);
        ic3.setTypeface(weatherFont);
        ic4 = (TextView) findViewById(R.id.forecast_icon_four);
        ic4.setTypeface(weatherFont);
        ic5 = (TextView) findViewById(R.id.forecast_icon_five);
        ic5.setTypeface(weatherFont);
        ic6 = (TextView) findViewById(R.id.forecast_icon_six);
        ic6.setTypeface(weatherFont);
        ic7 = (TextView) findViewById(R.id.forecast_icon_seven);
        ic7.setTypeface(weatherFont);
        ic8 = (TextView) findViewById(R.id.forecast_icon_eight);
        ic8.setTypeface(weatherFont);
        ic9 = (TextView) findViewById(R.id.forecast_icon_nine);
        ic9.setTypeface(weatherFont);
        ic10 = (TextView) findViewById(R.id.forecast_icon_ten);
        ic10.setTypeface(weatherFont);

        panelCity = (TextView) findViewById(R.id.panel_city_view);
        panelWeather = (TextView) findViewById(R.id.panel_weather_view);
        panelTemp = (TextView) findViewById(R.id.panel_temp_view);

        weather1 = (TextView) findViewById(R.id.forecast_code_one);
        weather2 = (TextView) findViewById(R.id.forecast_code_two);
        weather3 = (TextView) findViewById(R.id.forecast_code_three);
        weather4 = (TextView) findViewById(R.id.forecast_code_four);
        weather5 = (TextView) findViewById(R.id.forecast_code_five);
        weather6 = (TextView) findViewById(R.id.forecast_code_six);
        weather7 = (TextView) findViewById(R.id.forecast_code_seven);
        weather8 = (TextView) findViewById(R.id.forecast_code_eight);
        weather9 = (TextView) findViewById(R.id.forecast_code_nine);
        weather10 = (TextView) findViewById(R.id.forecast_code_ten);

        day1 = (TextView) findViewById(R.id.forecast_day_one);
        day2 = (TextView) findViewById(R.id.forecast_day_two);
        day3 = (TextView) findViewById(R.id.forecast_day_three);
        day4 = (TextView) findViewById(R.id.forecast_day_four);
        day5 = (TextView) findViewById(R.id.forecast_day_five);
        day6 = (TextView) findViewById(R.id.forecast_day_six);
        day7 = (TextView) findViewById(R.id.forecast_day_seven);
        day8 = (TextView) findViewById(R.id.forecast_day_eight);
        day9 = (TextView) findViewById(R.id.forecast_day_nine);
        day10 = (TextView) findViewById(R.id.forecast_day_ten);

        date1 = (TextView) findViewById(R.id.forecast_date_one);
        date2 = (TextView) findViewById(R.id.forecast_date_two);
        date3 = (TextView) findViewById(R.id.forecast_date_three);
        date4 = (TextView) findViewById(R.id.forecast_date_four);
        date5 = (TextView) findViewById(R.id.forecast_date_five);
        date6 = (TextView) findViewById(R.id.forecast_date_six);
        date7 = (TextView) findViewById(R.id.forecast_date_seven);
        date8 = (TextView) findViewById(R.id.forecast_date_eight);
        date9 = (TextView) findViewById(R.id.forecast_date_nine);
        date10 = (TextView) findViewById(R.id.forecast_date_ten);

        high1 = (TextView) findViewById(R.id.forecast_high_one);
        high2 = (TextView) findViewById(R.id.forecast_high_two);
        high3 = (TextView) findViewById(R.id.forecast_high_three);
        high4 = (TextView) findViewById(R.id.forecast_high_four);
        high5 = (TextView) findViewById(R.id.forecast_high_five);
        high6 = (TextView) findViewById(R.id.forecast_high_six);
        high7 = (TextView) findViewById(R.id.forecast_high_seven);
        high8 = (TextView) findViewById(R.id.forecast_high_eight);
        high9 = (TextView) findViewById(R.id.forecast_high_nine);
        high10 = (TextView) findViewById(R.id.forecast_high_ten);

        low1 = (TextView) findViewById(R.id.forecast_low_one);
        low2 = (TextView) findViewById(R.id.forecast_low_two);
        low3 = (TextView) findViewById(R.id.forecast_low_three);
        low4 = (TextView) findViewById(R.id.forecast_low_four);
        low5 = (TextView) findViewById(R.id.forecast_low_five);
        low6 = (TextView) findViewById(R.id.forecast_low_six);
        low7 = (TextView) findViewById(R.id.forecast_low_seven);
        low8 = (TextView) findViewById(R.id.forecast_low_eight);
        low9 = (TextView) findViewById(R.id.forecast_low_nine);
        low10 = (TextView) findViewById(R.id.forecast_low_ten);

        weatherUpdate  = (ImageView) findViewById(R.id.ic_update_weather);
        weatherUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.isNetworkConnected(context)){
                    getRefreshWeather();
                }else{
                    showSnack();
                }
            }
        });

        isPanelColse = false;
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                switch (newState.toString()){
                    case "EXPANDED":
                        isPanelOpen = false;
                        if(!isPanelColse){
                            isPanelColse = true;
                            YoYo.with(Techniques.FadeOut)
                                    .duration(300)
                                    .playOn(findViewById(R.id.panel_city_view));
                            YoYo.with(Techniques.FadeOut)
                                    .duration(300)
                                    .playOn(findViewById(R.id.panel_temp_view));
                            YoYo.with(Techniques.FadeOut)
                                    .duration(300)
                                    .playOn(findViewById(R.id.panel_weather_view));
                            YoYo.with(Techniques.FlipOutX)
                                    .duration(300)
                                    .playOn(findViewById(R.id.uparrow));
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    upArrow.setBackgroundResource(R.drawable.ic_close);
                                    YoYo.with(Techniques.FlipInX)
                                            .duration(400)
                                            .playOn(findViewById(R.id.uparrow));
                                }
                            }, 300);
                        }
                        break;
                    case "COLLAPSED":
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchContent.getWindowToken(), 0);
                        mLayout.setTouchEnabled(true);
                        isPanelColse = false;
                        if(!isPanelOpen){
                            YoYo.with(Techniques.FadeInUp)
                                    .duration(700)
                                    .playOn(findViewById(R.id.panel_city_view));
                            YoYo.with(Techniques.FadeInUp)
                                    .duration(700)
                                    .playOn(findViewById(R.id.panel_temp_view));
                            YoYo.with(Techniques.Landing)
                                    .duration(700)
                                    .playOn(findViewById(R.id.panel_weather_view));
                            isPanelOpen = true;
                            YoYo.with(Techniques.FlipOutX)
                                    .duration(300)
                                    .playOn(findViewById(R.id.uparrow));
                            final Handler handler2 = new Handler();
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    upArrow.setBackgroundResource(R.drawable.ic_uparrow);
                                    YoYo.with(Techniques.FlipInX)
                                            .duration(400)
                                            .playOn(findViewById(R.id.uparrow));
                                }
                            }, 300);
                        }
                        break;
                }
            }
        });

        int versionCode = BuildConfig.VERSION_CODE;

        fakeadd = (RelativeLayout) findViewById(R.id.fake_add);

        if(Config.FirstRunOfApp == false){
            Config.firts_run_of_app(context);
        }

        firstRunMain = getSharedPreferences("com.vmagnify.persiancalendar", MODE_PRIVATE);
        if(firstRunMain.getBoolean("firstrun", true)){
            firstRunMain.edit().putBoolean("firstrun", false).commit();
            intro_layout.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideInDown)
                    .duration(300)
                    .playOn(intro_layout);
            final Handler handlerIntro = new Handler();
            handlerIntro.postDelayed(new Runnable() {
                @Override
                public void run() {
                    YoYo.with(Techniques.SlideOutUp)
                            .duration(400)
                            .playOn(intro_layout);
                    final Handler handler10 = new Handler();
                    handler10.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           intro_layout.setVisibility(View.GONE);
                        }
                    }, 400);
                }
            }, 10000);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else {
            toolbar.setPadding(0, 0, 0, 0);
        }

        RecyclerView navigation = (RecyclerView) findViewById(R.id.navigation_view);
        navigation.setHasFixedSize(true);
        adapter = new DrawerAdapter(this);
        navigation.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        navigation.setLayoutManager(layoutManager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final View appMainView = findViewById(R.id.app_main_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            int slidingDirection = +1;

            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (isRTL())
                        slidingDirection = -1;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    slidingAnimation(drawerView, slideOffset);
                }
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            private void slidingAnimation(View drawerView, float slideOffset) {
                appMainView.setTranslationX(slideOffset * drawerView.getWidth() * slidingDirection);
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        selectItem(DEFAULT);

        LocalBroadcastManager.getInstance(this).registerReceiver(dayPassedReceiver,
                new IntentFilter(Constants.LOCAL_INTENT_DAY_PASSED));


        ChromeLikeSwipeLayout.makeConfig()
                .addIcon(R.drawable.ic_alarm_add_white_24dp)
                .addIcon(R.drawable.ic_note_add_white_24dp)
                .addIcon(R.drawable.ic_list_white_24dp)
                .addIcon(R.drawable.ic_camera_alt_white_24dp)
                .backgroundColor(getResources().getColor(R.color.primary))
                .listenItemSelected(new ChromeLikeSwipeLayout.IOnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        switch (index){
                            case 0:
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        startActivity(new Intent(MainActivity.this, CreateEditActivity.class));
                                    }
                                }, 300);
                                break;
                            case 1:
                                Intent intent = new Intent(MainActivity.this, MainActivityPep.class);
                                intent.putExtra("NewNoteFromMainActivity",true);
                                startActivity(intent);
                                break;
                            case 2:
                                Intent intent2 = new Intent(MainActivity.this, MainActivityPep.class);
                                intent2.putExtra("NewNoteFromMainActivityList",true);
                                startActivity(intent2);
                                break;
                            case 3:
                                Intent intent3 = new Intent(MainActivity.this, MainActivityPep.class);
                                intent3.putExtra("NewNoteFromMainActivityCamera",true);
                                startActivity(intent3);
                                break;
                        }
                    }
                })
                .setTo(chromeLikeSwipeLayout);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean isRTL() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        utils.changeAppLanguage(this);
        View v = findViewById(R.id.drawer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            v.setLayoutDirection(isRTL() ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        }
    }

    public boolean dayIsPassed = false;

    private BroadcastReceiver dayPassedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dayIsPassed = true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(!Config.FirstRun){
            CITY = "tehran";
            temp_inorder = "c";

        }else {
            updateWeatherView();
            selectItem(DEFAULT);
        }
        if (dayIsPassed) {
            dayIsPassed = false;
            restartActivity();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dayPassedReceiver);
        super.onDestroy();
    }

    public void onClickItem(int position) {
        selectItem(position);
    }

    @Override
    public void onBackPressed() {
        if (menuPosition != DEFAULT) {
            selectItem(DEFAULT);
        }else if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Checking for the "menu" key
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void beforeMenuChange(int position) {
        if (position != menuPosition) {
            // reset app lang on menu changes, ugly hack but it seems is needed
            utils.changeAppLanguage(this);
        }

        // only if we are returning from preferences
        if (menuPosition != PREFERENCE)
            return;

        utils.updateStoredPreference();
        updateUtils.update(true);

        boolean needsActivityRestart = false;

        String locale = utils.getAppLanguage();
        if (!locale.equals(lastLocale)) {
            lastLocale = locale;
            utils.changeAppLanguage(this);
            utils.loadLanguageResource();
            needsActivityRestart = true;
        }

        if (!lastTheme.equals(utils.getTheme())) {
            needsActivityRestart = true;
            lastTheme = utils.getTheme();
        }

        if (needsActivityRestart)
            restartActivity();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void selectItem(int item) {

        switch (item){

            case CALENDAR:
                drawerLayout.closeDrawers();
                showPanel();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        beforeMenuChange(CALENDAR);
                        if (menuPosition != CALENDAR) {
                            try {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(
                                                R.id.fragment_holder,
                                                (Fragment) fragments[CALENDAR].newInstance(),
                                                fragments[CALENDAR].getName()
                                        ).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            menuPosition = CALENDAR;
                        }

                    }
                }, 250);

                adapter.setSelectedItem(CALENDAR);
                break;

            case TODO:

                startActivity(new Intent(MainActivity.this, com.recurrence.activities.MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case NOTE:

                startActivity(new Intent(MainActivity.this, MainActivityPep.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case EXPENSE_MANAGER:

                startActivity(new Intent(MainActivity.this, com.outlay.view.activity.MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case CONVERTER:
                drawerLayout.closeDrawers();
                hidePanel();
                beforeMenuChange(CONVERTER - 3);
                if (menuPosition != CONVERTER- 3) {
                    try {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.fragment_holder,
                                        (Fragment) fragments[CONVERTER-3].newInstance(),
                                        fragments[CONVERTER-3].getName()
                                ).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    menuPosition = CONVERTER-3;
                }

                adapter.setSelectedItem(CONVERTER );
                break;

            case COMPASS:
                startActivity(new Intent(MainActivity.this, MainStart.class));
                break;
            case PREFERENCE:
                drawerLayout.closeDrawers();
                hidePanel();
                beforeMenuChange(PREFERENCE -4);
                if (menuPosition != PREFERENCE -4 ) {
                    try {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.fragment_holder,
                                        (Fragment) fragments[PREFERENCE -4].newInstance(),
                                        fragments[PREFERENCE -4].getName()
                                ).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    menuPosition = PREFERENCE -4;
                }

                adapter.setSelectedItem(PREFERENCE);
                break;

            case EXIT:
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE)
            LocalBroadcastManager.getInstance(this).sendBroadcast(
                    new Intent(Constants.LOCATION_PERMISSION_RESULT));
    }

    public static void updateWeatherView(){


        cityName.setText(Config.get_city_weather(context, "").toString());
        panelCity.setText(Config.get_city_weather(context, "").toString());

        countryname.setText(Config.get_country_weather(context, "").toString());
        String weatherCode = Config.get__weather_code(context, "3200").toString();

        String wind_dir = Config.get_wind_direction(context, "0").toString();
        int wind_direction = Integer.parseInt(wind_dir);

        Animation an = new RotateAnimation(0.0f, wind_direction+.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        an.setDuration(2000);               // duration in ms
        an.setRepeatCount(0);                // -1 = infinite repeated
        an.setRepeatMode(Animation.REVERSE); // reverses each repeat
        an.setFillAfter(true);

        windDirection.setAnimation(an);

        String persian_windSpeed = ConvertPersianCal.toPersianNumber(Config.get_wind_speed(context, "").toString());
        windSpeed.setText( persian_windSpeed+ " " + Config.get_speed_unit(context, "").toString());

        String persian_humidity = ConvertPersianCal.toPersianNumber(Config.get_humidity(context,"").toString());
        humidity.setText(persian_humidity+"%");

        String char_pressure = Config.get_pressure(context,"").toString();
        String first3char = char_pressure.substring(0, 3);
        String pressure_persion = ConvertPersianCal.toPersianNumber(first3char);
        pressure.setText(pressure_persion+" "+Config.get_pressure_unit(context, "").toString());

        String persian_visibility = ConvertPersianCal.toPersianNumber(Config.get_visibility(context,"").toString());
        visibility.setText(persian_visibility+" "+Config.get_distance_unit(context, "").toString());
        lastUpdate.setText(Config.get__weather_last_update(context," "));
        String persian_temp = ConvertPersianCal.toPersianNumber(Config.get__weather_temp_condition(context,"").toString());
        temperature.setText(persian_temp+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));
        panelTemp.setText(persian_temp+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        sunrise.setText(Config.get_sunrise(context,"").toString());
        sunset.setText(Config.get_sunset(context,"").toString());

        switch(weatherCode) {
            case "0":
                weatherIcon.setText(R.string.tornado);
                weatherText.setText("گردباد");
                panelWeather.setText("گردباد");
                break;
            case "1":
                weatherIcon.setText(R.string.tropical_storm);
                weatherText.setText("طوفان گرمسیری");
                panelWeather.setText("طوفان گرمسیری");
                break;
            case "2":
                weatherIcon.setText(R.string.hurricane);
                weatherText.setText("طوفان");
                panelWeather.setText("طوفان");
                break;
            case "3":
                weatherIcon.setText(R.string.severe_thunderstorms);
                weatherText.setText("رعد و برق شدید");
                panelWeather.setText("رعد و برق شدید");
                break;
            case "4":
                weatherIcon.setText(R.string.severe_thunderstorms);
                weatherText.setText("رعد و برق");
                panelWeather.setText("رعد و برق");
                break;
            case "5":
                weatherIcon.setText(R.string.mixed_rain_and_snow);
                weatherText.setText("باران و برف مخلوط");
                panelWeather.setText("باران و برف مخلوط");
                break;
            case "6":
                weatherIcon.setText(R.string.mixed_rain_and_sleet);
                weatherText.setText("باران و برف مخلوط");
                panelWeather.setText("باران و برف مخلوط");
                break;
            case "7":
                weatherIcon.setText(R.string.mixed_snow_and_sleet);
                weatherText.setText("باران و برف مخلوط");
                panelWeather.setText("باران و برف مخلوط");
                break;
            case "8":
                weatherIcon.setText(R.string.freezing_drizzle);
                weatherText.setText("انجماد و نم نم باران");
                panelWeather.setText("انجماد و نم نم باران");
                break;
            case "9":
                weatherIcon.setText(R.string.drizzle);
                weatherText.setText("نم نم باران");
                panelWeather.setText("نم نم باران");
                break;
            case "10":
                weatherIcon.setText(R.string.freezing_rain);
                weatherText.setText("باران انجماد");
                panelWeather.setText("باران انجماد");
                break;
            case "11":
                weatherIcon.setText(R.string.showers);
                weatherText.setText("بارندگی");
                panelWeather.setText("بارندگی");
                break;
            case "12":
                weatherIcon.setText(R.string.showers);
                weatherText.setText("بارندگی");
                panelWeather.setText("بارندگی");
                break;
            case "13":
                weatherIcon.setText(R.string.snow_flurries);
                weatherText.setText("برف و وزش ناگهانی باد");
                panelWeather.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                weatherIcon.setText(R.string.light_snow_showers);
                weatherText.setText("بارش برف");
                panelWeather.setText("بارش برف");
                break;
            case "15":
                weatherIcon.setText(R.string.blowing_snow);
                weatherText.setText("بارش برف");
                panelWeather.setText("بارش برف");
                break;
            case "16":
                weatherIcon.setText(R.string.snow);
                weatherText.setText("بارش برف");
                panelWeather.setText("بارش برف");
                break;
            case "17":
                weatherIcon.setText(R.string.hail);
                weatherText.setText("تگرگ");
                panelWeather.setText("تگرگ");
                break;
            case "18":
                weatherIcon.setText(R.string.sleet);
                weatherText.setText("برف و باران");
                panelWeather.setText("برف و باران");
                break;
            case "19":
                weatherIcon.setText(R.string.dust);
                weatherText.setText("گرد و خاک");
                panelWeather.setText("گرد و خاک");
                break;
            case "20":
                weatherIcon.setText(R.string.foggy);
                weatherText.setText("مه الود");
                panelWeather.setText("مه الود");
                break;
            case "21":
                weatherIcon.setText(R.string.haze);
                weatherText.setText("مه");
                panelWeather.setText("مه");
                break;
            case "22":
                weatherIcon.setText(R.string.smoky);
                weatherText.setText("دود آلود");
                panelWeather.setText("دود آلود");
                break;
            case "23":
                weatherIcon.setText(R.string.blustery);
                weatherText.setText("پر باد");
                panelWeather.setText("پر باد");
                break;
            case "24":
                weatherIcon.setText(R.string.windy);
                weatherText.setText("پر باد");
                panelWeather.setText("پر باد");
                break;
            case "25":
                weatherIcon.setText(R.string.cold);
                weatherText.setText("سرد");
                panelWeather.setText("سرد");
                break;
            case "26":
                weatherIcon.setText(R.string.cloudy);
                weatherText.setText("ابری");
                panelWeather.setText("ابری");
                break;
            case "27":
                weatherIcon.setText(R.string.mostly_cloudy_night);
                weatherText.setText("ابری (شب)");
                panelWeather.setText("ابری (شب)");
                break;
            case "28":
                weatherIcon.setText(R.string.mostly_cloudy_day);
                weatherText.setText("ابری (روز)");
                panelWeather.setText("ابری (روز)");
                break;
            case "29":
                weatherIcon.setText(R.string.partly_cloudy_night);
                weatherText.setText("قسمتی ابری (شب)");
                panelWeather.setText("قسمتی ابری (شب)");
                break;
            case "30":
                weatherIcon.setText(R.string.partly_cloudy_day);
                weatherText.setText("قسمتی ابری (روز)");
                panelWeather.setText("قسمتی ابری (روز)");
                break;
            case "31":
                weatherIcon.setText(R.string.clear_night);
                weatherText.setText("هوای صاف (شب)");
                panelWeather.setText("هوای صاف (شب)");
                break;
            case "32":
                weatherIcon.setText(R.string.sunny);
                weatherText.setText("آفتابی");
                panelWeather.setText("آفتابی");
                break;
            case "33":
                weatherIcon.setText(R.string.fair_night);
                weatherText.setText("شب صاف");
                panelWeather.setText("شب صاف");
                break;
            case "34":
                weatherIcon.setText(R.string.fair_day);
                weatherText.setText("روز صاف");
                panelWeather.setText("روز صاف");
                break;
            case "35":
                weatherIcon.setText(R.string.mixed_rain_and_hail);
                weatherText.setText("باران و تگرگ مخلوط");
                panelWeather.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                weatherIcon.setText(R.string.hot);
                weatherText.setText("گرم");
                panelWeather.setText("گرم");
                break;
            case "37":
                weatherIcon.setText(R.string.isolated_thundershowers);
                weatherText.setText("رعدوبرق پراکنده");
                panelWeather.setText("رعدوبرق پراکنده");
                break;
            case "38":
                weatherIcon.setText(R.string.scattered_thunderstorms);
                weatherText.setText("رعد و برق پراکنده");
                panelWeather.setText("رعد و برق پراکنده");
                break;
            case "39":
                weatherIcon.setText(R.string.scattered_thunderstorms);
                weatherText.setText("رعد و برق پراکنده");
                panelWeather.setText("رعد و برق پراکنده");
                break;
            case "40":
                weatherIcon.setText(R.string.scattered_showers);
                weatherText.setText("رگبار پراکنده");
                panelWeather.setText("رگبار پراکنده");
                break;
            case "41":
                weatherIcon.setText(R.string.heavy_snow);
                weatherText.setText("برف سنگین");
                panelWeather.setText("برف سنگین");
                break;
            case "42":
                weatherIcon.setText(R.string.scattered_snow_showers);
                weatherText.setText("بارش برف پراکنده");
                panelWeather.setText("بارش برف پراکنده");
                break;
            case "43":
                weatherIcon.setText(R.string.heavy_snow);
                weatherText.setText("برف سنگین");
                panelWeather.setText("برف سنگین");
                break;
            case "44":
                weatherIcon.setText(R.string.partly_cloudy);
                weatherText.setText("قسمتی ابری");
                panelWeather.setText("قسمتی ابری");
                break;
            case "45":
                weatherIcon.setText(R.string.thundershowers);
                weatherText.setText("رعد و برق");
                panelWeather.setText("رعد و برق");
                break;
            case "46":
                weatherIcon.setText(R.string.snow_showers);
                weatherText.setText("بارش برف");
                panelWeather.setText("بارش برف");
                break;
            case "47":
                weatherIcon.setText(R.string.isolated_thundershowers);
                weatherText.setText("رعد و برق پراکنده");
                panelWeather.setText("رعد و برق پراکنده");
                break;
            default:
                weatherIcon.setText(R.string.not_available);
                weatherText.setText("");
                panelWeather.setText("");
                break;

        }

        ////// day1 /////
        String get_day1 = ForecastConfig.get_WeatherForcast_day_day1(context,"").toString();
        switch (get_day1){
            case "Mon":
                day1.setText("دوشنبه");
                break;
            case "Tue":
                day1.setText("سه شنبه");
                break;
            case "Wed":
                day1.setText("چهارشنبه");
                break;
            case "Thu":
                day1.setText("پنجشنبه");
                break;
            case "Fri":
                day1.setText("جمعه");
                break;
            case "Sat":
                day1.setText("شنبه");
                break;
            case "Sun":
                day1.setText("یکشنبه");
                break;
        }
        date1.setText(ForecastConfig.get_WeatherForcast_date_day1(context, "").toString());

        String persian_temp_high1 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day1(context, "").toString());
        high1.setText(persian_temp_high1+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low1 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day1(context, "").toString());
        low1.setText(persian_temp_low1+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast1 = ForecastConfig.get_WeatherForcast_code_day1(context,"").toString();
        switch(get_weather_forecast1) {
            case "0":
                ic1.setText(R.string.tornado);
                weather1.setText("گردباد");
                break;
            case "1":
                ic1.setText(R.string.tropical_storm);
                weather1.setText("طوفان گرمسیری");
                break;
            case "2":
                ic1.setText(R.string.tornado);
                weather1.setText("طوفان");
                break;
            case "3":
                ic1.setText(R.string.severe_thunderstorms);
                weather1.setText("رعد و برق شدید");
                break;
            case "4":
                ic1.setText(R.string.severe_thunderstorms);
                weather1.setText("رعد و برق");
                break;
            case "5":
                ic1.setText(R.string.mixed_rain_and_snow);
                weather1.setText("باران و برف مخلوط");
                break;
            case "6":
                ic1.setText(R.string.mixed_rain_and_snow);
                weather1.setText("باران و برف مخلوط");
                break;
            case "7":
                ic1.setText(R.string.mixed_rain_and_snow);
                weather1.setText("باران و برف مخلوط");
                break;
            case "8":
                ic1.setText(R.string.freezing_drizzle);
                weather1.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic1.setText(R.string.drizzle);
                weather1.setText("نم نم باران");
                break;
            case "10":
                ic1.setText(R.string.freezing_drizzle);
                weather1.setText("باران انجماد");
                break;
            case "11":
                ic1.setText(R.string.showers);
                weather1.setText("بارندگی");
                break;
            case "12":
                ic1.setText(R.string.showers);
                weather1.setText("بارندگی");
                break;
            case "13":
                ic1.setText(R.string.snow_flurries);
                weather1.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic1.setText(R.string.light_snow_showers);
                weather1.setText("بارش برف");
                break;
            case "15":
                ic1.setText(R.string.light_snow_showers);
                weather1.setText("بارش برف");
                break;
            case "16":
                ic1.setText(R.string.light_snow_showers);
                weather1.setText("بارش برف");
                break;
            case "17":
                ic1.setText(R.string.hail);
                weather1.setText("تگرگ");
                break;
            case "18":
                ic1.setText(R.string.mixed_rain_and_snow);
                weather1.setText("برف و باران");
                break;
            case "19":
                ic1.setText(R.string.dust);
                weather1.setText("گرد و خاک");
                break;
            case "20":
                ic1.setText(R.string.foggy);
                weather1.setText("مه الود");
                break;
            case "21":
                ic1.setText(R.string.foggy);
                weather1.setText("مه");
                break;
            case "22":
                ic1.setText(R.string.foggy);
                weather1.setText("دود آلود");
                break;
            case "23":
                ic1.setText(R.string.blustery);
                weather1.setText("پر باد");
                break;
            case "24":
                ic1.setText(R.string.blustery);
                weather1.setText("پر باد");
                break;
            case "25":
                ic1.setText(R.string.cold);
                weather1.setText("سرد");
                break;
            case "26":
                ic1.setText(R.string.cloudy);
                weather1.setText("ابری");
                break;
            case "27":
                ic1.setText(R.string.mostly_cloudy_night);
                weather1.setText("ابری (شب)");
                break;
            case "28":
                ic1.setText(R.string.mostly_cloudy_day);
                weather1.setText("ابری (روز)");
                break;
            case "29":
                ic1.setText(R.string.mostly_cloudy_night);
                weather1.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic1.setText(R.string.mostly_cloudy_day);
                weather1.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic1.setText(R.string.clear_night);
                weather1.setText("هوای صاف (شب)");
                break;
            case "32":
                ic1.setText(R.string.sunny);
                weather1.setText("آفتابی");
                break;
            case "33":
                ic1.setText(R.string.clear_night);
                weather1.setText("شب صاف");
                break;
            case "34":
                ic1.setText(R.string.sunny);
                weather1.setText("روز صاف");
                break;
            case "35":
                ic1.setText(R.string.hail);
                weather1.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic1.setText(R.string.hot);
                weather1.setText("گرم");
                break;
            case "37":
                ic1.setText(R.string.isolated_thundershowers);
                weather1.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic1.setText(R.string.isolated_thundershowers);
                weather1.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic1.setText(R.string.isolated_thundershowers);
                weather1.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic1.setText(R.string.drizzle);
                weather1.setText("رگبار پراکنده");
                break;
            case "41":
                ic1.setText(R.string.light_snow_showers);
                weather1.setText("برف سنگین");
                break;
            case "42":
                ic1.setText(R.string.light_snow_showers);
                weather1.setText("بارش برف پراکنده");
                break;
            case "43":
                ic1.setText(R.string.light_snow_showers);
                weather1.setText("برف سنگین");
                break;
            case "44":
                ic1.setText(R.string.mostly_cloudy_day);
                weather1.setText("قسمتی ابری");
                break;
            case "45":
                ic1.setText(R.string.severe_thunderstorms);
                weather1.setText("رعد و برق");
                break;
            case "46":
                ic1.setText(R.string.mixed_rain_and_snow);
                weather1.setText("بارش برف");
                break;
            case "47":
                ic1.setText(R.string.isolated_thundershowers);
                weather1.setText("رعد و برق پراکنده");
                break;
            default:
                ic1.setText(R.string.not_available);
                weather1.setText("");
                break;

        }

        ////// day2 /////
        String get_day2 = ForecastConfig.get_WeatherForcast_day_day2(context, "").toString();
        switch (get_day2){
            case "Mon":
                day2.setText("دوشنبه");
                break;
            case "Tue":
                day2.setText("سه شنبه");
                break;
            case "Wed":
                day2.setText("چهارشنبه");
                break;
            case "Thu":
                day2.setText("پنجشنبه");
                break;
            case "Fri":
                day2.setText("جمعه");
                break;
            case "Sat":
                day2.setText("شنبه");
                break;
            case "Sun":
                day2.setText("یکشنبه");
                break;
        }
        date2.setText(ForecastConfig.get_WeatherForcast_date_day2(context, "").toString());

        String persian_temp_high2 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day2(context, "").toString());
        high2.setText(persian_temp_high2+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low2 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day2(context, "").toString());
        low2.setText(persian_temp_low2+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast2 = ForecastConfig.get_WeatherForcast_code_day2(context, "").toString();
        switch(get_weather_forecast2) {
            case "0":
                ic2.setText(R.string.tornado);
                weather2.setText("گردباد");
                break;
            case "1":
                ic2.setText(R.string.tropical_storm);
                weather2.setText("طوفان گرمسیری");
                break;
            case "2":
                ic2.setText(R.string.tornado);
                weather2.setText("طوفان");
                break;
            case "3":
                ic2.setText(R.string.severe_thunderstorms);
                weather2.setText("رعد و برق شدید");
                break;
            case "4":
                ic2.setText(R.string.severe_thunderstorms);
                weather2.setText("رعد و برق");
                break;
            case "5":
                ic2.setText(R.string.mixed_rain_and_snow);
                weather2.setText("باران و برف مخلوط");
                break;
            case "6":
                ic2.setText(R.string.mixed_rain_and_snow);
                weather2.setText("باران و برف مخلوط");
                break;
            case "7":
                ic2.setText(R.string.mixed_rain_and_snow);
                weather2.setText("باران و برف مخلوط");
                break;
            case "8":
                ic2.setText(R.string.freezing_drizzle);
                weather2.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic2.setText(R.string.drizzle);
                weather2.setText("نم نم باران");
                break;
            case "10":
                ic2.setText(R.string.freezing_drizzle);
                weather2.setText("باران انجماد");
                break;
            case "11":
                ic2.setText(R.string.showers);
                weather2.setText("بارندگی");
                break;
            case "12":
                ic2.setText(R.string.showers);
                weather2.setText("بارندگی");
                break;
            case "13":
                ic2.setText(R.string.snow_flurries);
                weather2.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic2.setText(R.string.light_snow_showers);
                weather2.setText("بارش برف");
                break;
            case "15":
                ic2.setText(R.string.light_snow_showers);
                weather2.setText("بارش برف");
                break;
            case "16":
                ic2.setText(R.string.light_snow_showers);
                weather2.setText("بارش برف");
                break;
            case "17":
                ic2.setText(R.string.hail);
                weather2.setText("تگرگ");
                break;
            case "18":
                ic2.setText(R.string.mixed_rain_and_snow);
                weather2.setText("برف و باران");
                break;
            case "19":
                ic2.setText(R.string.dust);
                weather2.setText("گرد و خاک");
                break;
            case "20":
                ic2.setText(R.string.foggy);
                weather2.setText("مه الود");
                break;
            case "21":
                ic2.setText(R.string.foggy);
                weather2.setText("مه");
                break;
            case "22":
                ic2.setText(R.string.foggy);
                weather2.setText("دود آلود");
                break;
            case "23":
                ic2.setText(R.string.blustery);
                weather2.setText("پر باد");
                break;
            case "24":
                ic2.setText(R.string.blustery);
                weather2.setText("پر باد");
                break;
            case "25":
                ic2.setText(R.string.cold);
                weather2.setText("سرد");
                break;
            case "26":
                ic2.setText(R.string.cloudy);
                weather2.setText("ابری");
                break;
            case "27":
                ic2.setText(R.string.mostly_cloudy_night);
                weather2.setText("ابری (شب)");
                break;
            case "28":
                ic2.setText(R.string.mostly_cloudy_day);
                weather2.setText("ابری (روز)");
                break;
            case "29":
                ic2.setText(R.string.mostly_cloudy_night);
                weather2.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic2.setText(R.string.mostly_cloudy_day);
                weather2.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic2.setText(R.string.clear_night);
                weather2.setText("هوای صاف (شب)");
                break;
            case "32":
                ic2.setText(R.string.sunny);
                weather2.setText("آفتابی");
                break;
            case "33":
                ic2.setText(R.string.clear_night);
                weather2.setText("شب صاف");
                break;
            case "34":
                ic2.setText(R.string.sunny);
                weather2.setText("روز صاف");
                break;
            case "35":
                ic2.setText(R.string.hail);
                weather2.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic2.setText(R.string.hot);
                weather2.setText("گرم");
                break;
            case "37":
                ic2.setText(R.string.isolated_thundershowers);
                weather2.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic2.setText(R.string.isolated_thundershowers);
                weather2.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic2.setText(R.string.isolated_thundershowers);
                weather2.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic2.setText(R.string.drizzle);
                weather2.setText("رگبار پراکنده");
                break;
            case "41":
                ic2.setText(R.string.light_snow_showers);
                weather2.setText("برف سنگین");
                break;
            case "42":
                ic2.setText(R.string.light_snow_showers);
                weather2.setText("بارش برف پراکنده");
                break;
            case "43":
                ic2.setText(R.string.light_snow_showers);
                weather2.setText("برف سنگین");
                break;
            case "44":
                ic2.setText(R.string.mostly_cloudy_day);
                weather2.setText("قسمتی ابری");
                break;
            case "45":
                ic2.setText(R.string.severe_thunderstorms);
                weather2.setText("رعد و برق");
                break;
            case "46":
                ic2.setText(R.string.mixed_rain_and_snow);
                weather2.setText("بارش برف");
                break;
            case "47":
                ic2.setText(R.string.isolated_thundershowers);
                weather2.setText("رعد و برق پراکنده");
                break;
            default:
                ic2.setText(R.string.not_available);
                weather2.setText("");
                break;
        }

        ////// day2 /////
        String get_day3 = ForecastConfig.get_WeatherForcast_day_day3(context, "").toString();
        switch (get_day3){
            case "Mon":
                day3.setText("دوشنبه");
                break;
            case "Tue":
                day3.setText("سه شنبه");
                break;
            case "Wed":
                day3.setText("چهارشنبه");
                break;
            case "Thu":
                day3.setText("پنجشنبه");
                break;
            case "Fri":
                day3.setText("جمعه");
                break;
            case "Sat":
                day3.setText("شنبه");
                break;
            case "Sun":
                day3.setText("یکشنبه");
                break;
        }
        date3.setText(ForecastConfig.get_WeatherForcast_date_day3(context, "").toString());

        String persian_temp_high3 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day3(context, "").toString());
        high3.setText(persian_temp_high3+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low3 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day3(context, "").toString());
        low3.setText(persian_temp_low3+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast3 = ForecastConfig.get_WeatherForcast_code_day3(context, "").toString();
        switch(get_weather_forecast3) {
            case "0":
                ic3.setText(R.string.tornado);
                weather3.setText("گردباد");
                break;
            case "1":
                ic3.setText(R.string.tropical_storm);
                weather3.setText("طوفان گرمسیری");
                break;
            case "2":
                ic3.setText(R.string.tornado);
                weather3.setText("طوفان");
                break;
            case "3":
                ic3.setText(R.string.severe_thunderstorms);
                weather3.setText("رعد و برق شدید");
                break;
            case "4":
                ic3.setText(R.string.severe_thunderstorms);
                weather3.setText("رعد و برق");
                break;
            case "5":
                ic3.setText(R.string.mixed_rain_and_snow);
                weather3.setText("باران و برف مخلوط");
                break;
            case "6":
                ic3.setText(R.string.mixed_rain_and_snow);
                weather3.setText("باران و برف مخلوط");
                break;
            case "7":
                ic3.setText(R.string.mixed_rain_and_snow);
                weather3.setText("باران و برف مخلوط");
                break;
            case "8":
                ic3.setText(R.string.freezing_drizzle);
                weather3.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic3.setText(R.string.drizzle);
                weather3.setText("نم نم باران");
                break;
            case "10":
                ic3.setText(R.string.freezing_drizzle);
                weather3.setText("باران انجماد");
                break;
            case "11":
                ic3.setText(R.string.showers);
                weather3.setText("بارندگی");
                break;
            case "12":
                ic3.setText(R.string.showers);
                weather3.setText("بارندگی");
                break;
            case "13":
                ic3.setText(R.string.snow_flurries);
                weather3.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic3.setText(R.string.light_snow_showers);
                weather3.setText("بارش برف");
                break;
            case "15":
                ic3.setText(R.string.light_snow_showers);
                weather3.setText("بارش برف");
                break;
            case "16":
                ic3.setText(R.string.light_snow_showers);
                weather3.setText("بارش برف");
                break;
            case "17":
                ic3.setText(R.string.hail);
                weather3.setText("تگرگ");
                break;
            case "18":
                ic3.setText(R.string.mixed_rain_and_snow);
                weather3.setText("برف و باران");
                break;
            case "19":
                ic3.setText(R.string.dust);
                weather3.setText("گرد و خاک");
                break;
            case "20":
                ic3.setText(R.string.foggy);
                weather3.setText("مه الود");
                break;
            case "21":
                ic3.setText(R.string.foggy);
                weather3.setText("مه");
                break;
            case "22":
                ic3.setText(R.string.foggy);
                weather3.setText("دود آلود");
                break;
            case "23":
                ic3.setText(R.string.blustery);
                weather3.setText("پر باد");
                break;
            case "24":
                ic3.setText(R.string.blustery);
                weather3.setText("پر باد");
                break;
            case "25":
                ic3.setText(R.string.cold);
                weather3.setText("سرد");
                break;
            case "26":
                ic3.setText(R.string.cloudy);
                weather3.setText("ابری");
                break;
            case "27":
                ic3.setText(R.string.mostly_cloudy_night);
                weather3.setText("ابری (شب)");
                break;
            case "28":
                ic3.setText(R.string.mostly_cloudy_day);
                weather3.setText("ابری (روز)");
                break;
            case "29":
                ic3.setText(R.string.mostly_cloudy_night);
                weather3.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic3.setText(R.string.mostly_cloudy_day);
                weather3.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic3.setText(R.string.clear_night);
                weather3.setText("هوای صاف (شب)");
                break;
            case "32":
                ic3.setText(R.string.sunny);
                weather3.setText("آفتابی");
                break;
            case "33":
                ic3.setText(R.string.clear_night);
                weather3.setText("شب صاف");
                break;
            case "34":
                ic3.setText(R.string.sunny);
                weather3.setText("روز صاف");
                break;
            case "35":
                ic3.setText(R.string.hail);
                weather3.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic3.setText(R.string.hot);
                weather3.setText("گرم");
                break;
            case "37":
                ic3.setText(R.string.isolated_thundershowers);
                weather3.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic3.setText(R.string.isolated_thundershowers);
                weather3.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic3.setText(R.string.isolated_thundershowers);
                weather3.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic3.setText(R.string.drizzle);
                weather3.setText("رگبار پراکنده");
                break;
            case "41":
                ic3.setText(R.string.light_snow_showers);
                weather3.setText("برف سنگین");
                break;
            case "42":
                ic3.setText(R.string.light_snow_showers);
                weather3.setText("بارش برف پراکنده");
                break;
            case "43":
                ic3.setText(R.string.light_snow_showers);
                weather3.setText("برف سنگین");
                break;
            case "44":
                ic3.setText(R.string.mostly_cloudy_day);
                weather3.setText("قسمتی ابری");
                break;
            case "45":
                ic3.setText(R.string.severe_thunderstorms);
                weather3.setText("رعد و برق");
                break;
            case "46":
                ic3.setText(R.string.mixed_rain_and_snow);
                weather3.setText("بارش برف");
                break;
            case "47":
                ic3.setText(R.string.isolated_thundershowers);
                weather3.setText("رعد و برق پراکنده");
                break;
            default:
                ic3.setText(R.string.not_available);
                weather3.setText("");
                break;
        }

        ////// day4 /////
        String get_day4 = ForecastConfig.get_WeatherForcast_day_day4(context, "").toString();
        switch (get_day4){
            case "Mon":
                day4.setText("دوشنبه");
                break;
            case "Tue":
                day4.setText("سه شنبه");
                break;
            case "Wed":
                day4.setText("چهارشنبه");
                break;
            case "Thu":
                day4.setText("پنجشنبه");
                break;
            case "Fri":
                day4.setText("جمعه");
                break;
            case "Sat":
                day4.setText("شنبه");
                break;
            case "Sun":
                day4.setText("یکشنبه");
                break;
        }
        date4.setText(ForecastConfig.get_WeatherForcast_date_day4(context, "").toString());

        String persian_temp_high4 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day4(context, "").toString());
        high4.setText(persian_temp_high4+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low4 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day4(context, "").toString());
        low4.setText(persian_temp_low4+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast4 = ForecastConfig.get_WeatherForcast_code_day4(context, "").toString();
        switch(get_weather_forecast4) {
            case "0":
                ic4.setText(R.string.tornado);
                weather4.setText("گردباد");
                break;
            case "1":
                ic4.setText(R.string.tropical_storm);
                weather4.setText("طوفان گرمسیری");
                break;
            case "2":
                ic4.setText(R.string.tornado);
                weather4.setText("طوفان");
                break;
            case "3":
                ic4.setText(R.string.severe_thunderstorms);
                weather4.setText("رعد و برق شدید");
                break;
            case "4":
                ic4.setText(R.string.severe_thunderstorms);
                weather4.setText("رعد و برق");
                break;
            case "5":
                ic4.setText(R.string.mixed_rain_and_snow);
                weather4.setText("باران و برف مخلوط");
                break;
            case "6":
                ic4.setText(R.string.mixed_rain_and_snow);
                weather4.setText("باران و برف مخلوط");
                break;
            case "7":
                ic4.setText(R.string.mixed_rain_and_snow);
                weather4.setText("باران و برف مخلوط");
                break;
            case "8":
                ic4.setText(R.string.freezing_drizzle);
                weather4.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic4.setText(R.string.drizzle);
                weather4.setText("نم نم باران");
                break;
            case "10":
                ic4.setText(R.string.freezing_drizzle);
                weather4.setText("باران انجماد");
                break;
            case "11":
                ic4.setText(R.string.showers);
                weather4.setText("بارندگی");
                break;
            case "12":
                ic4.setText(R.string.showers);
                weather4.setText("بارندگی");
                break;
            case "13":
                ic4.setText(R.string.snow_flurries);
                weather4.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic4.setText(R.string.light_snow_showers);
                weather4.setText("بارش برف");
                break;
            case "15":
                ic4.setText(R.string.light_snow_showers);
                weather4.setText("بارش برف");
                break;
            case "16":
                ic4.setText(R.string.light_snow_showers);
                weather4.setText("بارش برف");
                break;
            case "17":
                ic4.setText(R.string.hail);
                weather4.setText("تگرگ");
                break;
            case "18":
                ic4.setText(R.string.mixed_rain_and_snow);
                weather4.setText("برف و باران");
                break;
            case "19":
                ic4.setText(R.string.dust);
                weather4.setText("گرد و خاک");
                break;
            case "20":
                ic4.setText(R.string.foggy);
                weather4.setText("مه الود");
                break;
            case "21":
                ic4.setText(R.string.foggy);
                weather4.setText("مه");
                break;
            case "22":
                ic4.setText(R.string.foggy);
                weather4.setText("دود آلود");
                break;
            case "23":
                ic4.setText(R.string.blustery);
                weather4.setText("پر باد");
                break;
            case "24":
                ic4.setText(R.string.blustery);
                weather4.setText("پر باد");
                break;
            case "25":
                ic4.setText(R.string.cold);
                weather4.setText("سرد");
                break;
            case "26":
                ic4.setText(R.string.cloudy);
                weather4.setText("ابری");
                break;
            case "27":
                ic4.setText(R.string.mostly_cloudy_night);
                weather4.setText("ابری (شب)");
                break;
            case "28":
                ic4.setText(R.string.mostly_cloudy_day);
                weather4.setText("ابری (روز)");
                break;
            case "29":
                ic4.setText(R.string.mostly_cloudy_night);
                weather4.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic4.setText(R.string.mostly_cloudy_day);
                weather4.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic4.setText(R.string.clear_night);
                weather4.setText("هوای صاف (شب)");
                break;
            case "32":
                ic4.setText(R.string.sunny);
                weather4.setText("آفتابی");
                break;
            case "33":
                ic4.setText(R.string.clear_night);
                weather4.setText("شب صاف");
                break;
            case "34":
                ic4.setText(R.string.sunny);
                weather4.setText("روز صاف");
                break;
            case "35":
                ic4.setText(R.string.hail);
                weather4.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic4.setText(R.string.hot);
                weather4.setText("گرم");
                break;
            case "37":
                ic4.setText(R.string.isolated_thundershowers);
                weather4.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic4.setText(R.string.isolated_thundershowers);
                weather4.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic4.setText(R.string.isolated_thundershowers);
                weather4.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic4.setText(R.string.drizzle);
                weather4.setText("رگبار پراکنده");
                break;
            case "41":
                ic4.setText(R.string.light_snow_showers);
                weather4.setText("برف سنگین");
                break;
            case "42":
                ic4.setText(R.string.light_snow_showers);
                weather4.setText("بارش برف پراکنده");
                break;
            case "43":
                ic4.setText(R.string.light_snow_showers);
                weather4.setText("برف سنگین");
                break;
            case "44":
                ic4.setText(R.string.mostly_cloudy_day);
                weather4.setText("قسمتی ابری");
                break;
            case "45":
                ic4.setText(R.string.severe_thunderstorms);
                weather4.setText("رعد و برق");
                break;
            case "46":
                ic4.setText(R.string.mixed_rain_and_snow);
                weather4.setText("بارش برف");
                break;
            case "47":
                ic4.setText(R.string.isolated_thundershowers);
                weather4.setText("رعد و برق پراکنده");
                break;
            default:
                ic4.setText(R.string.not_available);
                weather4.setText("");
                break;
        }

        ////// day5 /////
        String get_day5 = ForecastConfig.get_WeatherForcast_day_day5(context, "").toString();
        switch (get_day5){
            case "Mon":
                day5.setText("دوشنبه");
                break;
            case "Tue":
                day5.setText("سه شنبه");
                break;
            case "Wed":
                day5.setText("چهارشنبه");
                break;
            case "Thu":
                day5.setText("پنجشنبه");
                break;
            case "Fri":
                day5.setText("جمعه");
                break;
            case "Sat":
                day5.setText("شنبه");
                break;
            case "Sun":
                day5.setText("یکشنبه");
                break;
        }
        date5.setText(ForecastConfig.get_WeatherForcast_date_day5(context, "").toString());

        String persian_temp_high5 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day5(context, "").toString());
        high5.setText(persian_temp_high5+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low5 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day5(context, "").toString());
        low5.setText(persian_temp_low5+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast5 = ForecastConfig.get_WeatherForcast_code_day5(context, "").toString();
        switch(get_weather_forecast5) {
            case "0":
                ic5.setText(R.string.tornado);
                weather5.setText("گردباد");
                break;
            case "1":
                ic5.setText(R.string.tropical_storm);
                weather5.setText("طوفان گرمسیری");
                break;
            case "2":
                ic5.setText(R.string.tornado);
                weather5.setText("طوفان");
                break;
            case "3":
                ic5.setText(R.string.severe_thunderstorms);
                weather5.setText("رعد و برق شدید");
                break;
            case "4":
                ic5.setText(R.string.severe_thunderstorms);
                weather5.setText("رعد و برق");
                break;
            case "5":
                ic5.setText(R.string.mixed_rain_and_snow);
                weather5.setText("باران و برف مخلوط");
                break;
            case "6":
                ic5.setText(R.string.mixed_rain_and_snow);
                weather5.setText("باران و برف مخلوط");
                break;
            case "7":
                ic5.setText(R.string.mixed_rain_and_snow);
                weather5.setText("باران و برف مخلوط");
                break;
            case "8":
                ic5.setText(R.string.freezing_drizzle);
                weather5.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic5.setText(R.string.drizzle);
                weather5.setText("نم نم باران");
                break;
            case "10":
                ic5.setText(R.string.freezing_drizzle);
                weather5.setText("باران انجماد");
                break;
            case "11":
                ic5.setText(R.string.showers);
                weather5.setText("بارندگی");
                break;
            case "12":
                ic5.setText(R.string.showers);
                weather5.setText("بارندگی");
                break;
            case "13":
                ic5.setText(R.string.snow_flurries);
                weather5.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic5.setText(R.string.light_snow_showers);
                weather5.setText("بارش برف");
                break;
            case "15":
                ic5.setText(R.string.light_snow_showers);
                weather5.setText("بارش برف");
                break;
            case "16":
                ic5.setText(R.string.light_snow_showers);
                weather5.setText("بارش برف");
                break;
            case "17":
                ic5.setText(R.string.hail);
                weather5.setText("تگرگ");
                break;
            case "18":
                ic5.setText(R.string.mixed_rain_and_snow);
                weather5.setText("برف و باران");
                break;
            case "19":
                ic5.setText(R.string.dust);
                weather5.setText("گرد و خاک");
                break;
            case "20":
                ic5.setText(R.string.foggy);
                weather5.setText("مه الود");
                break;
            case "21":
                ic5.setText(R.string.foggy);
                weather5.setText("مه");
                break;
            case "22":
                ic5.setText(R.string.foggy);
                weather5.setText("دود آلود");
                break;
            case "23":
                ic5.setText(R.string.blustery);
                weather5.setText("پر باد");
                break;
            case "24":
                ic5.setText(R.string.blustery);
                weather5.setText("پر باد");
                break;
            case "25":
                ic5.setText(R.string.cold);
                weather5.setText("سرد");
                break;
            case "26":
                ic5.setText(R.string.cloudy);
                weather5.setText("ابری");
                break;
            case "27":
                ic5.setText(R.string.mostly_cloudy_night);
                weather5.setText("ابری (شب)");
                break;
            case "28":
                ic5.setText(R.string.mostly_cloudy_day);
                weather5.setText("ابری (روز)");
                break;
            case "29":
                ic5.setText(R.string.mostly_cloudy_night);
                weather5.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic5.setText(R.string.mostly_cloudy_day);
                weather5.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic5.setText(R.string.clear_night);
                weather5.setText("هوای صاف (شب)");
                break;
            case "32":
                ic5.setText(R.string.sunny);
                weather5.setText("آفتابی");
                break;
            case "33":
                ic5.setText(R.string.clear_night);
                weather5.setText("شب صاف");
                break;
            case "34":
                ic5.setText(R.string.sunny);
                weather5.setText("روز صاف");
                break;
            case "35":
                ic5.setText(R.string.hail);
                weather5.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic5.setText(R.string.hot);
                weather5.setText("گرم");
                break;
            case "37":
                ic5.setText(R.string.isolated_thundershowers);
                weather5.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic5.setText(R.string.isolated_thundershowers);
                weather5.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic5.setText(R.string.isolated_thundershowers);
                weather5.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic5.setText(R.string.drizzle);
                weather5.setText("رگبار پراکنده");
                break;
            case "41":
                ic5.setText(R.string.light_snow_showers);
                weather5.setText("برف سنگین");
                break;
            case "42":
                ic5.setText(R.string.light_snow_showers);
                weather5.setText("بارش برف پراکنده");
                break;
            case "43":
                ic5.setText(R.string.light_snow_showers);
                weather5.setText("برف سنگین");
                break;
            case "44":
                ic5.setText(R.string.mostly_cloudy_day);
                weather5.setText("قسمتی ابری");
                break;
            case "45":
                ic5.setText(R.string.severe_thunderstorms);
                weather5.setText("رعد و برق");
                break;
            case "46":
                ic5.setText(R.string.mixed_rain_and_snow);
                weather5.setText("بارش برف");
                break;
            case "47":
                ic5.setText(R.string.isolated_thundershowers);
                weather5.setText("رعد و برق پراکنده");
                break;
            default:
                ic5.setText(R.string.not_available);
                weather5.setText("");
                break;
        }

        ////// day6 /////
        String get_day6 = ForecastConfig.get_WeatherForcast_day_day6(context, "").toString();
        switch (get_day6){
            case "Mon":
                day6.setText("دوشنبه");
                break;
            case "Tue":
                day6.setText("سه شنبه");
                break;
            case "Wed":
                day6.setText("چهارشنبه");
                break;
            case "Thu":
                day6.setText("پنجشنبه");
                break;
            case "Fri":
                day6.setText("جمعه");
                break;
            case "Sat":
                day6.setText("شنبه");
                break;
            case "Sun":
                day6.setText("یکشنبه");
                break;
        }
        date6.setText(ForecastConfig.get_WeatherForcast_date_day6(context, "").toString());

        String persian_temp_high6 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day6(context, "").toString());
        high6.setText(persian_temp_high6+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low6 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day6(context, "").toString());
        low6.setText(persian_temp_low6+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast6 = ForecastConfig.get_WeatherForcast_code_day6(context, "").toString();
        switch(get_weather_forecast6) {
            case "0":
                ic6.setText(R.string.tornado);
                weather6.setText("گردباد");
                break;
            case "1":
                ic6.setText(R.string.tropical_storm);
                weather6.setText("طوفان گرمسیری");
                break;
            case "2":
                ic6.setText(R.string.tornado);
                weather6.setText("طوفان");
                break;
            case "3":
                ic6.setText(R.string.severe_thunderstorms);
                weather6.setText("رعد و برق شدید");
                break;
            case "4":
                ic6.setText(R.string.severe_thunderstorms);
                weather6.setText("رعد و برق");
                break;
            case "5":
                ic6.setText(R.string.mixed_rain_and_snow);
                weather6.setText("باران و برف مخلوط");
                break;
            case "6":
                ic6.setText(R.string.mixed_rain_and_snow);
                weather6.setText("باران و برف مخلوط");
                break;
            case "7":
                ic6.setText(R.string.mixed_rain_and_snow);
                weather6.setText("باران و برف مخلوط");
                break;
            case "8":
                ic6.setText(R.string.freezing_drizzle);
                weather6.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic6.setText(R.string.drizzle);
                weather6.setText("نم نم باران");
                break;
            case "10":
                ic6.setText(R.string.freezing_drizzle);
                weather6.setText("باران انجماد");
                break;
            case "11":
                ic6.setText(R.string.showers);
                weather6.setText("بارندگی");
                break;
            case "12":
                ic6.setText(R.string.showers);
                weather6.setText("بارندگی");
                break;
            case "13":
                ic6.setText(R.string.snow_flurries);
                weather6.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic6.setText(R.string.light_snow_showers);
                weather6.setText("بارش برف");
                break;
            case "15":
                ic6.setText(R.string.light_snow_showers);
                weather6.setText("بارش برف");
                break;
            case "16":
                ic6.setText(R.string.light_snow_showers);
                weather6.setText("بارش برف");
                break;
            case "17":
                ic6.setText(R.string.hail);
                weather6.setText("تگرگ");
                break;
            case "18":
                ic6.setText(R.string.mixed_rain_and_snow);
                weather6.setText("برف و باران");
                break;
            case "19":
                ic6.setText(R.string.dust);
                weather6.setText("گرد و خاک");
                break;
            case "20":
                ic6.setText(R.string.foggy);
                weather6.setText("مه الود");
                break;
            case "21":
                ic6.setText(R.string.foggy);
                weather6.setText("مه");
                break;
            case "22":
                ic6.setText(R.string.foggy);
                weather6.setText("دود آلود");
                break;
            case "23":
                ic6.setText(R.string.blustery);
                weather6.setText("پر باد");
                break;
            case "24":
                ic6.setText(R.string.blustery);
                weather6.setText("پر باد");
                break;
            case "25":
                ic6.setText(R.string.cold);
                weather6.setText("سرد");
                break;
            case "26":
                ic6.setText(R.string.cloudy);
                weather6.setText("ابری");
                break;
            case "27":
                ic6.setText(R.string.mostly_cloudy_night);
                weather6.setText("ابری (شب)");
                break;
            case "28":
                ic6.setText(R.string.mostly_cloudy_day);
                weather6.setText("ابری (روز)");
                break;
            case "29":
                ic6.setText(R.string.mostly_cloudy_night);
                weather6.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic6.setText(R.string.mostly_cloudy_day);
                weather6.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic6.setText(R.string.clear_night);
                weather6.setText("هوای صاف (شب)");
                break;
            case "32":
                ic6.setText(R.string.sunny);
                weather6.setText("آفتابی");
                break;
            case "33":
                ic6.setText(R.string.clear_night);
                weather6.setText("شب صاف");
                break;
            case "34":
                ic6.setText(R.string.sunny);
                weather6.setText("روز صاف");
                break;
            case "35":
                ic6.setText(R.string.hail);
                weather6.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic6.setText(R.string.hot);
                weather6.setText("گرم");
                break;
            case "37":
                ic6.setText(R.string.isolated_thundershowers);
                weather6.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic6.setText(R.string.isolated_thundershowers);
                weather6.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic6.setText(R.string.isolated_thundershowers);
                weather6.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic6.setText(R.string.drizzle);
                weather6.setText("رگبار پراکنده");
                break;
            case "41":
                ic6.setText(R.string.light_snow_showers);
                weather6.setText("برف سنگین");
                break;
            case "42":
                ic6.setText(R.string.light_snow_showers);
                weather6.setText("بارش برف پراکنده");
                break;
            case "43":
                ic6.setText(R.string.light_snow_showers);
                weather6.setText("برف سنگین");
                break;
            case "44":
                ic6.setText(R.string.mostly_cloudy_day);
                weather6.setText("قسمتی ابری");
                break;
            case "45":
                ic6.setText(R.string.severe_thunderstorms);
                weather6.setText("رعد و برق");
                break;
            case "46":
                ic6.setText(R.string.mixed_rain_and_snow);
                weather6.setText("بارش برف");
                break;
            case "47":
                ic6.setText(R.string.isolated_thundershowers);
                weather6.setText("رعد و برق پراکنده");
                break;
            default:
                ic6.setText(R.string.not_available);
                weather6.setText("");
                break;
        }

        ////// day7 /////
        String get_day7 = ForecastConfig.get_WeatherForcast_day_day7(context, "").toString();
        switch (get_day7){
            case "Mon":
                day7.setText("دوشنبه");
                break;
            case "Tue":
                day7.setText("سه شنبه");
                break;
            case "Wed":
                day7.setText("چهارشنبه");
                break;
            case "Thu":
                day7.setText("پنجشنبه");
                break;
            case "Fri":
                day7.setText("جمعه");
                break;
            case "Sat":
                day7.setText("شنبه");
                break;
            case "Sun":
                day7.setText("یکشنبه");
                break;
        }
        date7.setText(ForecastConfig.get_WeatherForcast_date_day7(context, "").toString());

        String persian_temp_high7 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day7(context, "").toString());
        high7.setText(persian_temp_high7+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low7 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day7(context, "").toString());
        low7.setText(persian_temp_low7+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast7 = ForecastConfig.get_WeatherForcast_code_day7(context, "").toString();
        switch(get_weather_forecast7) {
            case "0":
                ic7.setText(R.string.tornado);
                weather7.setText("گردباد");
                break;
            case "1":
                ic7.setText(R.string.tropical_storm);
                weather7.setText("طوفان گرمسیری");
                break;
            case "2":
                ic7.setText(R.string.tornado);
                weather7.setText("طوفان");
                break;
            case "3":
                ic7.setText(R.string.severe_thunderstorms);
                weather7.setText("رعد و برق شدید");
                break;
            case "4":
                ic7.setText(R.string.severe_thunderstorms);
                weather7.setText("رعد و برق");
                break;
            case "5":
                ic7.setText(R.string.mixed_rain_and_snow);
                weather7.setText("باران و برف مخلوط");
                break;
            case "6":
                ic7.setText(R.string.mixed_rain_and_snow);
                weather7.setText("باران و برف مخلوط");
                break;
            case "7":
                ic7.setText(R.string.mixed_rain_and_snow);
                weather7.setText("باران و برف مخلوط");
                break;
            case "8":
                ic7.setText(R.string.freezing_drizzle);
                weather7.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic7.setText(R.string.drizzle);
                weather7.setText("نم نم باران");
                break;
            case "10":
                ic7.setText(R.string.freezing_drizzle);
                weather7.setText("باران انجماد");
                break;
            case "11":
                ic7.setText(R.string.showers);
                weather7.setText("بارندگی");
                break;
            case "12":
                ic7.setText(R.string.showers);
                weather7.setText("بارندگی");
                break;
            case "13":
                ic7.setText(R.string.snow_flurries);
                weather7.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic7.setText(R.string.light_snow_showers);
                weather7.setText("بارش برف");
                break;
            case "15":
                ic7.setText(R.string.light_snow_showers);
                weather7.setText("بارش برف");
                break;
            case "16":
                ic7.setText(R.string.light_snow_showers);
                weather7.setText("بارش برف");
                break;
            case "17":
                ic7.setText(R.string.hail);
                weather7.setText("تگرگ");
                break;
            case "18":
                ic7.setText(R.string.mixed_rain_and_snow);
                weather7.setText("برف و باران");
                break;
            case "19":
                ic7.setText(R.string.dust);
                weather7.setText("گرد و خاک");
                break;
            case "20":
                ic7.setText(R.string.foggy);
                weather7.setText("مه الود");
                break;
            case "21":
                ic7.setText(R.string.foggy);
                weather7.setText("مه");
                break;
            case "22":
                ic7.setText(R.string.foggy);
                weather7.setText("دود آلود");
                break;
            case "23":
                ic7.setText(R.string.blustery);
                weather7.setText("پر باد");
                break;
            case "24":
                ic7.setText(R.string.blustery);
                weather7.setText("پر باد");
                break;
            case "25":
                ic7.setText(R.string.cold);
                weather7.setText("سرد");
                break;
            case "26":
                ic7.setText(R.string.cloudy);
                weather7.setText("ابری");
                break;
            case "27":
                ic7.setText(R.string.mostly_cloudy_night);
                weather7.setText("ابری (شب)");
                break;
            case "28":
                ic7.setText(R.string.mostly_cloudy_day);
                weather7.setText("ابری (روز)");
                break;
            case "29":
                ic7.setText(R.string.mostly_cloudy_night);
                weather7.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic7.setText(R.string.mostly_cloudy_day);
                weather7.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic7.setText(R.string.clear_night);
                weather7.setText("هوای صاف (شب)");
                break;
            case "32":
                ic7.setText(R.string.sunny);
                weather7.setText("آفتابی");
                break;
            case "33":
                ic7.setText(R.string.clear_night);
                weather7.setText("شب صاف");
                break;
            case "34":
                ic7.setText(R.string.sunny);
                weather7.setText("روز صاف");
                break;
            case "35":
                ic7.setText(R.string.hail);
                weather7.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic7.setText(R.string.hot);
                weather7.setText("گرم");
                break;
            case "37":
                ic7.setText(R.string.isolated_thundershowers);
                weather7.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic7.setText(R.string.isolated_thundershowers);
                weather7.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic7.setText(R.string.isolated_thundershowers);
                weather7.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic7.setText(R.string.drizzle);
                weather7.setText("رگبار پراکنده");
                break;
            case "41":
                ic7.setText(R.string.light_snow_showers);
                weather7.setText("برف سنگین");
                break;
            case "42":
                ic7.setText(R.string.light_snow_showers);
                weather7.setText("بارش برف پراکنده");
                break;
            case "43":
                ic7.setText(R.string.light_snow_showers);
                weather7.setText("برف سنگین");
                break;
            case "44":
                ic7.setText(R.string.mostly_cloudy_day);
                weather7.setText("قسمتی ابری");
                break;
            case "45":
                ic7.setText(R.string.severe_thunderstorms);
                weather7.setText("رعد و برق");
                break;
            case "46":
                ic7.setText(R.string.mixed_rain_and_snow);
                weather7.setText("بارش برف");
                break;
            case "47":
                ic7.setText(R.string.isolated_thundershowers);
                weather7.setText("رعد و برق پراکنده");
                break;
            default:
                ic7.setText(R.string.not_available);
                weather7.setText("");
                break;
        }
        ////// day8 /////
        String get_day8 = ForecastConfig.get_WeatherForcast_day_day8(context, "").toString();
        switch (get_day8){
            case "Mon":
                day8.setText("دوشنبه");
                break;
            case "Tue":
                day8.setText("سه شنبه");
                break;
            case "Wed":
                day8.setText("چهارشنبه");
                break;
            case "Thu":
                day8.setText("پنجشنبه");
                break;
            case "Fri":
                day8.setText("جمعه");
                break;
            case "Sat":
                day8.setText("شنبه");
                break;
            case "Sun":
                day8.setText("یکشنبه");
                break;
        }
        date8.setText(ForecastConfig.get_WeatherForcast_date_day8(context, "").toString());

        String persian_temp_high8 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day8(context, "").toString());
        high8.setText(persian_temp_high8+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low8 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day8(context, "").toString());
        low8.setText(persian_temp_low8+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast8 = ForecastConfig.get_WeatherForcast_code_day8(context, "").toString();
        switch(get_weather_forecast8) {
            case "0":
                ic8.setText(R.string.tornado);
                weather8.setText("گردباد");
                break;
            case "1":
                ic8.setText(R.string.tropical_storm);
                weather8.setText("طوفان گرمسیری");
                break;
            case "2":
                ic8.setText(R.string.tornado);
                weather8.setText("طوفان");
                break;
            case "3":
                ic8.setText(R.string.severe_thunderstorms);
                weather8.setText("رعد و برق شدید");
                break;
            case "4":
                ic8.setText(R.string.severe_thunderstorms);
                weather8.setText("رعد و برق");
                break;
            case "5":
                ic8.setText(R.string.mixed_rain_and_snow);
                weather8.setText("باران و برف مخلوط");
                break;
            case "6":
                ic8.setText(R.string.mixed_rain_and_snow);
                weather8.setText("باران و برف مخلوط");
                break;
            case "7":
                ic8.setText(R.string.mixed_rain_and_snow);
                weather8.setText("باران و برف مخلوط");
                break;
            case "8":
                ic8.setText(R.string.freezing_drizzle);
                weather8.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic8.setText(R.string.drizzle);
                weather8.setText("نم نم باران");
                break;
            case "10":
                ic8.setText(R.string.freezing_drizzle);
                weather8.setText("باران انجماد");
                break;
            case "11":
                ic8.setText(R.string.showers);
                weather8.setText("بارندگی");
                break;
            case "12":
                ic8.setText(R.string.showers);
                weather8.setText("بارندگی");
                break;
            case "13":
                ic8.setText(R.string.snow_flurries);
                weather8.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic8.setText(R.string.light_snow_showers);
                weather8.setText("بارش برف");
                break;
            case "15":
                ic8.setText(R.string.light_snow_showers);
                weather8.setText("بارش برف");
                break;
            case "16":
                ic8.setText(R.string.light_snow_showers);
                weather8.setText("بارش برف");
                break;
            case "17":
                ic8.setText(R.string.hail);
                weather8.setText("تگرگ");
                break;
            case "18":
                ic8.setText(R.string.mixed_rain_and_snow);
                weather8.setText("برف و باران");
                break;
            case "19":
                ic8.setText(R.string.dust);
                weather8.setText("گرد و خاک");
                break;
            case "20":
                ic8.setText(R.string.foggy);
                weather8.setText("مه الود");
                break;
            case "21":
                ic8.setText(R.string.foggy);
                weather8.setText("مه");
                break;
            case "22":
                ic8.setText(R.string.foggy);
                weather8.setText("دود آلود");
                break;
            case "23":
                ic8.setText(R.string.blustery);
                weather8.setText("پر باد");
                break;
            case "24":
                ic8.setText(R.string.blustery);
                weather8.setText("پر باد");
                break;
            case "25":
                ic8.setText(R.string.cold);
                weather8.setText("سرد");
                break;
            case "26":
                ic8.setText(R.string.cloudy);
                weather8.setText("ابری");
                break;
            case "27":
                ic8.setText(R.string.mostly_cloudy_night);
                weather8.setText("ابری (شب)");
                break;
            case "28":
                ic8.setText(R.string.mostly_cloudy_day);
                weather8.setText("ابری (روز)");
                break;
            case "29":
                ic8.setText(R.string.mostly_cloudy_night);
                weather8.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic8.setText(R.string.mostly_cloudy_day);
                weather8.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic8.setText(R.string.clear_night);
                weather8.setText("هوای صاف (شب)");
                break;
            case "32":
                ic8.setText(R.string.sunny);
                weather8.setText("آفتابی");
                break;
            case "33":
                ic8.setText(R.string.clear_night);
                weather8.setText("شب صاف");
                break;
            case "34":
                ic8.setText(R.string.sunny);
                weather8.setText("روز صاف");
                break;
            case "35":
                ic8.setText(R.string.hail);
                weather8.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic8.setText(R.string.hot);
                weather8.setText("گرم");
                break;
            case "37":
                ic8.setText(R.string.isolated_thundershowers);
                weather8.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic8.setText(R.string.isolated_thundershowers);
                weather8.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic8.setText(R.string.isolated_thundershowers);
                weather8.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic8.setText(R.string.drizzle);
                weather8.setText("رگبار پراکنده");
                break;
            case "41":
                ic8.setText(R.string.light_snow_showers);
                weather8.setText("برف سنگین");
                break;
            case "42":
                ic8.setText(R.string.light_snow_showers);
                weather8.setText("بارش برف پراکنده");
                break;
            case "43":
                ic8.setText(R.string.light_snow_showers);
                weather8.setText("برف سنگین");
                break;
            case "44":
                ic8.setText(R.string.mostly_cloudy_day);
                weather8.setText("قسمتی ابری");
                break;
            case "45":
                ic8.setText(R.string.severe_thunderstorms);
                weather8.setText("رعد و برق");
                break;
            case "46":
                ic8.setText(R.string.mixed_rain_and_snow);
                weather8.setText("بارش برف");
                break;
            case "47":
                ic8.setText(R.string.isolated_thundershowers);
                weather8.setText("رعد و برق پراکنده");
                break;
            default:
                ic8.setText(R.string.not_available);
                weather8.setText("");
                break;
        }

        ////// day9 /////
        String get_day9 = ForecastConfig.get_WeatherForcast_day_day9(context, "").toString();
        switch (get_day9){
            case "Mon":
                day9.setText("دوشنبه");
                break;
            case "Tue":
                day9.setText("سه شنبه");
                break;
            case "Wed":
                day9.setText("چهارشنبه");
                break;
            case "Thu":
                day9.setText("پنجشنبه");
                break;
            case "Fri":
                day9.setText("جمعه");
                break;
            case "Sat":
                day9.setText("شنبه");
                break;
            case "Sun":
                day9.setText("یکشنبه");
                break;
        }
        date9.setText(ForecastConfig.get_WeatherForcast_date_day9(context, "").toString());

        String persian_temp_high9 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day9(context, "").toString());
        high9.setText(persian_temp_high9+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low9 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day9(context, "").toString());
        low9.setText(persian_temp_low9+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast9 = ForecastConfig.get_WeatherForcast_code_day9(context, "").toString();
        switch(get_weather_forecast9) {
            case "0":
                ic9.setText(R.string.tornado);
                weather9.setText("گردباد");
                break;
            case "1":
                ic9.setText(R.string.tropical_storm);
                weather9.setText("طوفان گرمسیری");
                break;
            case "2":
                ic9.setText(R.string.tornado);
                weather9.setText("طوفان");
                break;
            case "3":
                ic9.setText(R.string.severe_thunderstorms);
                weather9.setText("رعد و برق شدید");
                break;
            case "4":
                ic9.setText(R.string.severe_thunderstorms);
                weather9.setText("رعد و برق");
                break;
            case "5":
                ic9.setText(R.string.mixed_rain_and_snow);
                weather9.setText("باران و برف مخلوط");
                break;
            case "6":
                ic9.setText(R.string.mixed_rain_and_snow);
                weather9.setText("باران و برف مخلوط");
                break;
            case "7":
                ic9.setText(R.string.mixed_rain_and_snow);
                weather9.setText("باران و برف مخلوط");
                break;
            case "8":
                ic9.setText(R.string.freezing_drizzle);
                weather9.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic9.setText(R.string.drizzle);
                weather9.setText("نم نم باران");
                break;
            case "10":
                ic9.setText(R.string.freezing_drizzle);
                weather9.setText("باران انجماد");
                break;
            case "11":
                ic9.setText(R.string.showers);
                weather9.setText("بارندگی");
                break;
            case "12":
                ic9.setText(R.string.showers);
                weather9.setText("بارندگی");
                break;
            case "13":
                ic9.setText(R.string.snow_flurries);
                weather9.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic9.setText(R.string.light_snow_showers);
                weather9.setText("بارش برف");
                break;
            case "15":
                ic9.setText(R.string.light_snow_showers);
                weather9.setText("بارش برف");
                break;
            case "16":
                ic9.setText(R.string.light_snow_showers);
                weather9.setText("بارش برف");
                break;
            case "17":
                ic9.setText(R.string.hail);
                weather9.setText("تگرگ");
                break;
            case "18":
                ic9.setText(R.string.mixed_rain_and_snow);
                weather9.setText("برف و باران");
                break;
            case "19":
                ic9.setText(R.string.dust);
                weather9.setText("گرد و خاک");
                break;
            case "20":
                ic9.setText(R.string.foggy);
                weather9.setText("مه الود");
                break;
            case "21":
                ic9.setText(R.string.foggy);
                weather9.setText("مه");
                break;
            case "22":
                ic9.setText(R.string.foggy);
                weather9.setText("دود آلود");
                break;
            case "23":
                ic9.setText(R.string.blustery);
                weather9.setText("پر باد");
                break;
            case "24":
                ic9.setText(R.string.blustery);
                weather9.setText("پر باد");
                break;
            case "25":
                ic9.setText(R.string.cold);
                weather9.setText("سرد");
                break;
            case "26":
                ic9.setText(R.string.cloudy);
                weather9.setText("ابری");
                break;
            case "27":
                ic9.setText(R.string.mostly_cloudy_night);
                weather9.setText("ابری (شب)");
                break;
            case "28":
                ic9.setText(R.string.mostly_cloudy_day);
                weather9.setText("ابری (روز)");
                break;
            case "29":
                ic9.setText(R.string.mostly_cloudy_night);
                weather9.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic9.setText(R.string.mostly_cloudy_day);
                weather9.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic9.setText(R.string.clear_night);
                weather9.setText("هوای صاف (شب)");
                break;
            case "32":
                ic9.setText(R.string.sunny);
                weather9.setText("آفتابی");
                break;
            case "33":
                ic9.setText(R.string.clear_night);
                weather9.setText("شب صاف");
                break;
            case "34":
                ic9.setText(R.string.sunny);
                weather9.setText("روز صاف");
                break;
            case "35":
                ic9.setText(R.string.hail);
                weather9.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic9.setText(R.string.hot);
                weather9.setText("گرم");
                break;
            case "37":
                ic9.setText(R.string.isolated_thundershowers);
                weather9.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic9.setText(R.string.isolated_thundershowers);
                weather9.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic9.setText(R.string.isolated_thundershowers);
                weather9.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic9.setText(R.string.drizzle);
                weather9.setText("رگبار پراکنده");
                break;
            case "41":
                ic9.setText(R.string.light_snow_showers);
                weather9.setText("برف سنگین");
                break;
            case "42":
                ic9.setText(R.string.light_snow_showers);
                weather9.setText("بارش برف پراکنده");
                break;
            case "43":
                ic9.setText(R.string.light_snow_showers);
                weather9.setText("برف سنگین");
                break;
            case "44":
                ic9.setText(R.string.mostly_cloudy_day);
                weather9.setText("قسمتی ابری");
                break;
            case "45":
                ic9.setText(R.string.severe_thunderstorms);
                weather9.setText("رعد و برق");
                break;
            case "46":
                ic9.setText(R.string.mixed_rain_and_snow);
                weather9.setText("بارش برف");
                break;
            case "47":
                ic9.setText(R.string.isolated_thundershowers);
                weather9.setText("رعد و برق پراکنده");
                break;
            default:
                ic9.setText(R.string.not_available);
                weather9.setText("");
                break;
        }

        ////// day9 /////
        String get_day10 = ForecastConfig.get_WeatherForcast_day_day10(context, "").toString();
        switch (get_day10){
            case "Mon":
                day10.setText("دوشنبه");
                break;
            case "Tue":
                day10.setText("سه شنبه");
                break;
            case "Wed":
                day10.setText("چهارشنبه");
                break;
            case "Thu":
                day10.setText("پنجشنبه");
                break;
            case "Fri":
                day10.setText("جمعه");
                break;
            case "Sat":
                day10.setText("شنبه");
                break;
            case "Sun":
                day10.setText("یکشنبه");
                break;
        }
        date10.setText(ForecastConfig.get_WeatherForcast_date_day10(context, "").toString());

        String persian_temp_high10 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_high_day10(context, "").toString());
        high10.setText(persian_temp_high10+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String persian_temp_low10 = ConvertPersianCal.toPersianNumber(ForecastConfig.get_WeatherForcast_low_day10(context, "").toString());
        low10.setText(persian_temp_low10+" "+(char) 0x00B0+Config.get_temperature_unit(context,""));

        String get_weather_forecast10 = ForecastConfig.get_WeatherForcast_code_day10(context, "").toString();
        switch(get_weather_forecast10) {
            case "0":
                ic10.setText(R.string.tornado);
                weather10.setText("گردباد");
                break;
            case "1":
                ic10.setText(R.string.tropical_storm);
                weather10.setText("طوفان گرمسیری");
                break;
            case "2":
                ic10.setText(R.string.tornado);
                weather10.setText("طوفان");
                break;
            case "3":
                ic10.setText(R.string.severe_thunderstorms);
                weather10.setText("رعد و برق شدید");
                break;
            case "4":
                ic10.setText(R.string.severe_thunderstorms);
                weather10.setText("رعد و برق");
                break;
            case "5":
                ic10.setText(R.string.mixed_rain_and_snow);
                weather10.setText("باران و برف مخلوط");
                break;
            case "6":
                ic10.setText(R.string.mixed_rain_and_snow);
                weather10.setText("باران و برف مخلوط");
                break;
            case "7":
                ic10.setText(R.string.mixed_rain_and_snow);
                weather10.setText("باران و برف مخلوط");
                break;
            case "8":
                ic10.setText(R.string.freezing_drizzle);
                weather10.setText("انجماد و نم نم باران");
                break;
            case "9":
                ic10.setText(R.string.drizzle);
                weather10.setText("نم نم باران");
                break;
            case "10":
                ic10.setText(R.string.freezing_drizzle);
                weather10.setText("باران انجماد");
                break;
            case "11":
                ic10.setText(R.string.showers);
                weather10.setText("بارندگی");
                break;
            case "12":
                ic10.setText(R.string.showers);
                weather10.setText("بارندگی");
                break;
            case "13":
                ic10.setText(R.string.snow_flurries);
                weather10.setText("برف و وزش ناگهانی باد");
                break;
            case "14":
                ic10.setText(R.string.light_snow_showers);
                weather10.setText("بارش برف");
                break;
            case "15":
                ic10.setText(R.string.light_snow_showers);
                weather10.setText("بارش برف");
                break;
            case "16":
                ic10.setText(R.string.light_snow_showers);
                weather10.setText("بارش برف");
                break;
            case "17":
                ic10.setText(R.string.hail);
                weather10.setText("تگرگ");
                break;
            case "18":
                ic10.setText(R.string.mixed_rain_and_snow);
                weather10.setText("برف و باران");
                break;
            case "19":
                ic10.setText(R.string.dust);
                weather10.setText("گرد و خاک");
                break;
            case "20":
                ic10.setText(R.string.foggy);
                weather10.setText("مه الود");
                break;
            case "21":
                ic10.setText(R.string.foggy);
                weather10.setText("مه");
                break;
            case "22":
                ic10.setText(R.string.foggy);
                weather10.setText("دود آلود");
                break;
            case "23":
                ic10.setText(R.string.blustery);
                weather10.setText("پر باد");
                break;
            case "24":
                ic10.setText(R.string.blustery);
                weather10.setText("پر باد");
                break;
            case "25":
                ic10.setText(R.string.cold);
                weather10.setText("سرد");
                break;
            case "26":
                ic10.setText(R.string.cloudy);
                weather10.setText("ابری");
                break;
            case "27":
                ic10.setText(R.string.mostly_cloudy_night);
                weather10.setText("ابری (شب)");
                break;
            case "28":
                ic10.setText(R.string.mostly_cloudy_day);
                weather10.setText("ابری (روز)");
                break;
            case "29":
                ic10.setText(R.string.mostly_cloudy_night);
                weather10.setText("قسمتی ابری (شب)");
                break;
            case "30":
                ic10.setText(R.string.mostly_cloudy_day);
                weather10.setText("قسمتی ابری (روز)");
                break;
            case "31":
                ic10.setText(R.string.clear_night);
                weather10.setText("هوای صاف (شب)");
                break;
            case "32":
                ic10.setText(R.string.sunny);
                weather10.setText("آفتابی");
                break;
            case "33":
                ic10.setText(R.string.clear_night);
                weather10.setText("شب صاف");
                break;
            case "34":
                ic10.setText(R.string.sunny);
                weather10.setText("روز صاف");
                break;
            case "35":
                ic10.setText(R.string.hail);
                weather10.setText("باران و تگرگ مخلوط");
                break;
            case "36":
                ic10.setText(R.string.hot);
                weather10.setText("گرم");
                break;
            case "37":
                ic10.setText(R.string.isolated_thundershowers);
                weather10.setText("رعدوبرق پراکنده");
                break;
            case "38":
                ic10.setText(R.string.isolated_thundershowers);
                weather10.setText("رعد و برق پراکنده");
                break;
            case "39":
                ic10.setText(R.string.isolated_thundershowers);
                weather10.setText("رعد و برق پراکنده");
                break;
            case "40":
                ic10.setText(R.string.drizzle);
                weather10.setText("رگبار پراکنده");
                break;
            case "41":
                ic10.setText(R.string.light_snow_showers);
                weather10.setText("برف سنگین");
                break;
            case "42":
                ic10.setText(R.string.light_snow_showers);
                weather10.setText("بارش برف پراکنده");
                break;
            case "43":
                ic10.setText(R.string.light_snow_showers);
                weather10.setText("برف سنگین");
                break;
            case "44":
                ic10.setText(R.string.mostly_cloudy_day);
                weather10.setText("قسمتی ابری");
                break;
            case "45":
                ic10.setText(R.string.severe_thunderstorms);
                weather10.setText("رعد و برق");
                break;
            case "46":
                ic10.setText(R.string.mixed_rain_and_snow);
                weather10.setText("بارش برف");
                break;
            case "47":
                ic10.setText(R.string.isolated_thundershowers);
                weather10.setText("رعد و برق پراکنده");
                break;
            default:
                ic10.setText(R.string.not_available);
                weather10.setText("");
                break;
        }
    }

    public void getWeather(){

        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='"+temp_inorder+"'",CITY);
        String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

        AsyncHttpClient client;
        client = new AsyncHttpClient();
        client.setUserAgent(System.getProperty("http.agent", "Android device"));
        client.get(endPoint,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressBat.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        progressBat.dismiss();
                        try {
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progressBat.dismiss();
                        Config.updateLoaded_true(context);
                        try {

                            String year = ConvertPersianCal.yearPersian();
                            String day = ConvertPersianCal.dayPersian();
                            String mounth = ConvertPersianCal.mounthPersianCal();
                            String week = ConvertPersianCal.weekPersianCal();

                            Calendar c = Calendar.getInstance();
                            String hour = Integer.toString(c.get(Calendar.HOUR));
                            String persian_hour = ConvertPersianCal.toPersianNumber(hour);

                            String minute = Integer.toString(c.get(Calendar.MINUTE));
                            String persian_minute = ConvertPersianCal.toPersianNumber(minute);

                            Config.store_weather_last_update(context,week+" "+ConvertPersianCal.toPersianNumber(day)+" - "+mounth+" "+ConvertPersianCal.toPersianNumber(year)+"  "+persian_hour+":"+persian_minute);


                            ////// - location ///////
                            String WeatherCity = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("location").getString("city");
                            String WeatherCountry = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("location").getString("country");
                            String WeatherRegion = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("location").getString("region");

                            ////// - units ///////
                            String WeatherunitSpeed = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("units").getString("speed");
                            String WeatherTemperature = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("units").getString("temperature");
                            String WeatherPressure_unit = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("units").getString("pressure");
                            String WeatherDistance_unit = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("units").getString("distance");

                            ////// - wind ///////
                            String WeatherDirection = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("wind").getString("direction");
                            String Weatherwindspeed = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("wind").getString("speed");

                            ////// - atmosphere ///////
                            String WeatherHumidity = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("atmosphere").getString("humidity");
                            String WeatherPressure = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("atmosphere").getString("pressure");
                            String WeatherVisibility = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("atmosphere").getString("visibility");

                            ////// - astronomy ///////
                            String WeatherSunrise = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("astronomy").getString("sunrise");
                            String WeatherSunset = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("astronomy").getString("sunset");

                            ////// - astronomy ///////
                            String WeatherCode = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("code");
                            String Temperature = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("temp");
                            String WeatherText = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("text");

                            ////// - forecast ///////
                            String WeatherForcast_code_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day1(context, WeatherForcast_code_day1);
                            String WeatherForcast_date_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day1(context, WeatherForcast_date_day1);
                            String WeatherForcast_day_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day1(context, WeatherForcast_day_day1);
                            String WeatherForcast_high_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day1(context, WeatherForcast_high_day1);
                            String WeatherForcast_low_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day1(context, WeatherForcast_low_day1);


                            String WeatherForcast_code_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day2(context, WeatherForcast_code_day2);
                            String WeatherForcast_date_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day2(context, WeatherForcast_date_day2);
                            String WeatherForcast_day_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day2(context, WeatherForcast_day_day2);
                            String WeatherForcast_high_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day2(context, WeatherForcast_high_day2);
                            String WeatherForcast_low_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day2(context, WeatherForcast_low_day2);


                            String WeatherForcast_code_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day3(context, WeatherForcast_code_day3);
                            String WeatherForcast_date_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day3(context, WeatherForcast_date_day3);
                            String WeatherForcast_day_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day3(context, WeatherForcast_day_day3);
                            String WeatherForcast_high_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day3(context, WeatherForcast_high_day3);
                            String WeatherForcast_low_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day3(context, WeatherForcast_low_day3);

                            String WeatherForcast_code_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day4(context, WeatherForcast_code_day4);
                            String WeatherForcast_date_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day4(context, WeatherForcast_date_day4);
                            String WeatherForcast_day_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day4(context, WeatherForcast_day_day4);
                            String WeatherForcast_high_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day4(context, WeatherForcast_high_day4);
                            String WeatherForcast_low_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day4(context, WeatherForcast_low_day4);


                            String WeatherForcast_code_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day5(context, WeatherForcast_code_day5);
                            String WeatherForcast_date_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day5(context, WeatherForcast_date_day5);
                            String WeatherForcast_day_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day5(context, WeatherForcast_day_day5);
                            String WeatherForcast_high_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day5(context, WeatherForcast_high_day5);
                            String WeatherForcast_low_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day5(context, WeatherForcast_low_day5);


                            String WeatherForcast_code_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day6(context, WeatherForcast_code_day6);
                            String WeatherForcast_date_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day6(context, WeatherForcast_date_day6);
                            String WeatherForcast_day_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day6(context, WeatherForcast_day_day6);
                            String WeatherForcast_high_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day6(context, WeatherForcast_high_day6);
                            String WeatherForcast_low_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day6(context, WeatherForcast_low_day6);


                            String WeatherForcast_code_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day7(context, WeatherForcast_code_day7);
                            String WeatherForcast_date_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day7(context, WeatherForcast_date_day7);
                            String WeatherForcast_day_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day7(context, WeatherForcast_day_day7);
                            String WeatherForcast_high_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day7(context, WeatherForcast_high_day7);
                            String WeatherForcast_low_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day7(context, WeatherForcast_low_day7);


                            String WeatherForcast_code_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day8(context, WeatherForcast_code_day8);
                            String WeatherForcast_date_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day8(context, WeatherForcast_date_day8);
                            String WeatherForcast_day_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day8(context, WeatherForcast_day_day8);
                            String WeatherForcast_high_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day8(context, WeatherForcast_high_day8);
                            String WeatherForcast_low_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day8(context, WeatherForcast_low_day8);


                            String WeatherForcast_code_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day9(context, WeatherForcast_code_day9);
                            String WeatherForcast_date_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day9(context, WeatherForcast_date_day9);
                            String WeatherForcast_day_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day9(context, WeatherForcast_day_day9);
                            String WeatherForcast_high_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day9(context, WeatherForcast_high_day9);
                            String WeatherForcast_low_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day9(context, WeatherForcast_low_day9);


                            String WeatherForcast_code_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day10(context, WeatherForcast_code_day10);
                            String WeatherForcast_date_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day10(context, WeatherForcast_date_day10);
                            String WeatherForcast_day_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day10(context, WeatherForcast_day_day10);
                            String WeatherForcast_high_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day10(context, WeatherForcast_high_day10);
                            String WeatherForcast_low_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day10(context, WeatherForcast_low_day10);

                            ////// STORE DATA ///////

                            Config.store_city_weather(context,WeatherCity);
                            Config.store_country_weather(context, WeatherCountry);
                            Config.store_region_weather(context, WeatherRegion);

                            Config.store_speed_unit(context, WeatherunitSpeed);
                            Config.store_temperature_unit(context, WeatherTemperature);
                            Config.store_weather_temp_condition(context, Temperature);

                            Config.store_wind_direction(context, WeatherDirection);
                            Config.store_wind_speed(context, Weatherwindspeed);

                            Config.store_humidity(context, WeatherHumidity);
                            Config.store_pressure_unit(context, WeatherPressure_unit);
                            Config.store_pressure(context, WeatherPressure);
                            Config.store_visibility(context, WeatherVisibility);
                            Config.store_distance_unit(context, WeatherDistance_unit);

                            Config.store_sunrise(context, WeatherSunrise);
                            Config.store_sunset(context, WeatherSunset);

                            Config.store_weather_code(context, WeatherCode);
                            Config.store_weather_temp_numb(context, Temperature);
                            Config.store_weather_text_en(context, WeatherText);

                            Config.firts_run(context);
                            updateWeatherView();
                        } catch (Exception e) {

                        }
                        super.onSuccess(statusCode, headers, response);
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        progressBat.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Config.updateLoaded_false(context);
                        progressBat.dismiss();
                        showSnackerror();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Config.updateLoaded_false(context);
                        progressBat.dismiss();
                        showSnackerror();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Config.updateLoaded_false(context);
                        progressBat.dismiss();
                        showSnackerror();
                    }
                });
    }

    public static void showSnack(){

        Snackbar snackbar = Snackbar
                .make(slidingUpPanelLayout, "به اینترنت متصل نیستید !", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.errorColor));
        snackbar.show();
    }
    public static void showSnackerror(){

        Snackbar snackbar = Snackbar
                .make(slidingUpPanelLayout, "خطا در دریافت اطلاعات !", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.errorColor));
        snackbar.show();
    }
    public static void showFab(){
        backToMainDay.setShowAnimation(ActionButton.Animations.FADE_IN);
        backToMainDay.show();
    }
    public static void hideFab(){
        backToMainDay.setHideAnimation(ActionButton.Animations.FADE_OUT);
        backToMainDay.hide();
    }

    public static void hidePanel(){
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        CalendarFragment.hideSpace();
    }
    public static void showPanel(){
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        CalendarFragment.showSpace();
    }

    public void getRefreshWeather(){



        if(!Config.FirstRun){
            CITY = "tehran";
            temp_inorder = "c";

        }else {
            CITY = Config.get_city(MainActivity.this,"tehran");
            temp_inorder = Config.get_temperature_unit_inorder(MainActivity.this, "c");
        }
        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='"+temp_inorder+"'",CITY);
        String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

        AsyncHttpClient client;
        client = new AsyncHttpClient();
        client.setUserAgent(System.getProperty("http.agent", "Android device"));
        client.get(endPoint,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressBat.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        progressBat.dismiss();
                        try {
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Config.updateLoaded_true(MainActivity.this);
                        progressBat.dismiss();
                        try {

                            String year = ConvertPersianCal.yearPersian();
                            String day = ConvertPersianCal.dayPersian();
                            String mounth = ConvertPersianCal.mounthPersianCal();
                            String week = ConvertPersianCal.weekPersianCal();

                            Calendar c = Calendar.getInstance();
                            String hour = Integer.toString(c.get(Calendar.HOUR));
                            String persian_hour = ConvertPersianCal.toPersianNumber(hour);

                            String minute = Integer.toString(c.get(Calendar.MINUTE));
                            String persian_minute = ConvertPersianCal.toPersianNumber(minute);

                            Config.store_weather_last_update(MainActivity.this, week + " " + ConvertPersianCal.toPersianNumber(day) + " - " + mounth + " " + ConvertPersianCal.toPersianNumber(year) + "  " + persian_hour + ":" + persian_minute);


                            ////// - location ///////
                            String WeatherCity = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("location").getString("city");
                            String WeatherCountry = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("location").getString("country");
                            String WeatherRegion = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("location").getString("region");

                            ////// - units ///////
                            String WeatherunitSpeed = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("units").getString("speed");
                            String WeatherTemperature = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("units").getString("temperature");
                            String WeatherPressure_unit = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("units").getString("pressure");
                            String WeatherDistance_unit = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("units").getString("distance");

                            ////// - wind ///////
                            String WeatherDirection = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("wind").getString("direction");
                            String Weatherwindspeed = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("wind").getString("speed");

                            ////// - atmosphere ///////
                            String WeatherHumidity = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("atmosphere").getString("humidity");
                            String WeatherPressure = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("atmosphere").getString("pressure");
                            String WeatherVisibility = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("atmosphere").getString("visibility");

                            ////// - astronomy ///////
                            String WeatherSunrise = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("astronomy").getString("sunrise");
                            String WeatherSunset = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("astronomy").getString("sunset");

                            ////// - astronomy ///////
                            String WeatherCode = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("code");
                            String Temperature = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("temp");
                            String WeatherText = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("text");

                            ////// - forecast ///////
                            String WeatherForcast_code_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day1(MainActivity.this, WeatherForcast_code_day1);
                            String WeatherForcast_date_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day1(MainActivity.this, WeatherForcast_date_day1);
                            String WeatherForcast_day_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day1(MainActivity.this, WeatherForcast_day_day1);
                            String WeatherForcast_high_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day1(MainActivity.this, WeatherForcast_high_day1);
                            String WeatherForcast_low_day1 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day1(MainActivity.this, WeatherForcast_low_day1);


                            String WeatherForcast_code_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day2(MainActivity.this, WeatherForcast_code_day2);
                            String WeatherForcast_date_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day2(MainActivity.this, WeatherForcast_date_day2);
                            String WeatherForcast_day_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day2(MainActivity.this, WeatherForcast_day_day2);
                            String WeatherForcast_high_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day2(MainActivity.this, WeatherForcast_high_day2);
                            String WeatherForcast_low_day2 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day2(MainActivity.this, WeatherForcast_low_day2);


                            String WeatherForcast_code_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day3(MainActivity.this, WeatherForcast_code_day3);
                            String WeatherForcast_date_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day3(MainActivity.this, WeatherForcast_date_day3);
                            String WeatherForcast_day_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day3(MainActivity.this, WeatherForcast_day_day3);
                            String WeatherForcast_high_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day3(MainActivity.this, WeatherForcast_high_day3);
                            String WeatherForcast_low_day3 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day3(MainActivity.this, WeatherForcast_low_day3);

                            String WeatherForcast_code_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day4(MainActivity.this, WeatherForcast_code_day4);
                            String WeatherForcast_date_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day4(MainActivity.this, WeatherForcast_date_day4);
                            String WeatherForcast_day_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day4(MainActivity.this, WeatherForcast_day_day4);
                            String WeatherForcast_high_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day4(MainActivity.this, WeatherForcast_high_day4);
                            String WeatherForcast_low_day4 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day4(MainActivity.this, WeatherForcast_low_day4);


                            String WeatherForcast_code_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day5(MainActivity.this, WeatherForcast_code_day5);
                            String WeatherForcast_date_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day5(MainActivity.this, WeatherForcast_date_day5);
                            String WeatherForcast_day_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day5(MainActivity.this, WeatherForcast_day_day5);
                            String WeatherForcast_high_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day5(MainActivity.this, WeatherForcast_high_day5);
                            String WeatherForcast_low_day5 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day5(MainActivity.this, WeatherForcast_low_day5);


                            String WeatherForcast_code_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day6(MainActivity.this, WeatherForcast_code_day6);
                            String WeatherForcast_date_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day6(MainActivity.this, WeatherForcast_date_day6);
                            String WeatherForcast_day_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day6(MainActivity.this, WeatherForcast_day_day6);
                            String WeatherForcast_high_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day6(MainActivity.this, WeatherForcast_high_day6);
                            String WeatherForcast_low_day6 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day6(MainActivity.this, WeatherForcast_low_day6);


                            String WeatherForcast_code_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day7(MainActivity.this, WeatherForcast_code_day7);
                            String WeatherForcast_date_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day7(MainActivity.this, WeatherForcast_date_day7);
                            String WeatherForcast_day_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day7(MainActivity.this, WeatherForcast_day_day7);
                            String WeatherForcast_high_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day7(MainActivity.this, WeatherForcast_high_day7);
                            String WeatherForcast_low_day7 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day7(MainActivity.this, WeatherForcast_low_day7);


                            String WeatherForcast_code_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day8(MainActivity.this, WeatherForcast_code_day8);
                            String WeatherForcast_date_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day8(MainActivity.this, WeatherForcast_date_day8);
                            String WeatherForcast_day_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day8(MainActivity.this, WeatherForcast_day_day8);
                            String WeatherForcast_high_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day8(MainActivity.this, WeatherForcast_high_day8);
                            String WeatherForcast_low_day8 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day8(MainActivity.this, WeatherForcast_low_day8);


                            String WeatherForcast_code_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day9(MainActivity.this, WeatherForcast_code_day9);
                            String WeatherForcast_date_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day9(MainActivity.this, WeatherForcast_date_day9);
                            String WeatherForcast_day_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day9(MainActivity.this, WeatherForcast_day_day9);
                            String WeatherForcast_high_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day9(MainActivity.this, WeatherForcast_high_day9);
                            String WeatherForcast_low_day9 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day9(MainActivity.this, WeatherForcast_low_day9);


                            String WeatherForcast_code_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("code");
                            ForecastConfig.store_WeatherForcast_code_day10(MainActivity.this, WeatherForcast_code_day10);
                            String WeatherForcast_date_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("date");
                            ForecastConfig.store_WeatherForcast_date_day10(MainActivity.this, WeatherForcast_date_day10);
                            String WeatherForcast_day_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("day");
                            ForecastConfig.store_WeatherForcast_day_day10(MainActivity.this, WeatherForcast_day_day10);
                            String WeatherForcast_high_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("high");
                            ForecastConfig.store_WeatherForcast_high_day10(MainActivity.this, WeatherForcast_high_day10);
                            String WeatherForcast_low_day10 = response.getJSONObject("query").getJSONObject("results")
                                    .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("low");
                            ForecastConfig.store_WeatherForcast_low_day10(MainActivity.this, WeatherForcast_low_day10);

                            ////// STORE DATA ///////

                            Config.store_city_weather(MainActivity.this,WeatherCity);
                            Config.store_country_weather(MainActivity.this, WeatherCountry);
                            Config.store_region_weather(MainActivity.this, WeatherRegion);

                            Config.store_speed_unit(MainActivity.this, WeatherunitSpeed);
                            Config.store_temperature_unit(MainActivity.this, WeatherTemperature);
                            Config.store_weather_temp_condition(MainActivity.this, Temperature);

                            Config.store_wind_direction(MainActivity.this, WeatherDirection);
                            Config.store_wind_speed(MainActivity.this, Weatherwindspeed);

                            Config.store_humidity(MainActivity.this, WeatherHumidity);
                            Config.store_pressure_unit(MainActivity.this, WeatherPressure_unit);
                            Config.store_pressure(MainActivity.this, WeatherPressure);
                            Config.store_visibility(MainActivity.this, WeatherVisibility);
                            Config.store_distance_unit(MainActivity.this, WeatherDistance_unit);

                            Config.store_sunrise(MainActivity.this, WeatherSunrise);
                            Config.store_sunset(MainActivity.this, WeatherSunset);

                            Config.store_weather_code(MainActivity.this, WeatherCode);
                            Config.store_weather_temp_numb(MainActivity.this, Temperature);
                            Config.store_weather_text_en(MainActivity.this, WeatherText);

                            Config.firts_run(context);
                            MainActivity.updateWeatherView();

                        } catch (Exception e) {

                        }
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        progressBat.dismiss();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        try{
                            Config.updateLoaded_false(MainActivity.this);
                        }catch (Exception e){

                        }
                        showSnackerror();
                        progressBat.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try{
                            Config.updateLoaded_false(MainActivity.this);
                        }catch (Exception e){

                        }
                        showSnackerror();
                        progressBat.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Config.updateLoaded_false(MainActivity.this);
                        showSnackerror();
                        progressBat.dismiss();
                    }
                });
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

        }
        return true;
    }

    public static void getCityWeatherInfo(String city ){
        

            if(Config.isNetworkConnected(context)) {


                if (city.length() < 1) {
                   // error city.setError("شهر مورد نظر را وارد کنید.");
                    return;
                }
                Config.store_city(context, city);

                String CITY = Config.get_city(context, "tehran");
                String temp_inorder = Config.get_temperature_unit_inorder(context, "c");
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='" + temp_inorder + "'", CITY);
                String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                String searchQuery = Uri.encode(city);
                AsyncHttpClient client;
                client = new AsyncHttpClient();
                client.setUserAgent(System.getProperty("http.agent", "Android device"));
                client.get(endPoint,
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onStart() {
                                super.onStart();
                                progressBat.show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                progressBat.dismiss();
                                try {
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                progressBat.dismiss();
                                Config.updateLoaded_true(context);

                                try {

                                    String year = ConvertPersianCal.yearPersian();
                                    String day = ConvertPersianCal.dayPersian();
                                    String mounth = ConvertPersianCal.mounthPersianCal();
                                    String week = ConvertPersianCal.weekPersianCal();

                                    Calendar c = Calendar.getInstance();
                                    String hour = Integer.toString(c.get(Calendar.HOUR));
                                    String persian_hour = ConvertPersianCal.toPersianNumber(hour);

                                    String minute = Integer.toString(c.get(Calendar.MINUTE));
                                    String persian_minute = ConvertPersianCal.toPersianNumber(minute);

                                    Config.store_weather_last_update(context, week + " " + ConvertPersianCal.toPersianNumber(day) + " - " + mounth + " " + ConvertPersianCal.toPersianNumber(year) + "  " + persian_hour + ":" + persian_minute);

                                    String count = response.getJSONObject("query").getString("count");
                                    if(count.equalsIgnoreCase("0")){
                                        showSnackRongCity();
                                        return;
                                    }

                                    ////// - location ///////
                                    String WeatherCity = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("location").getString("city");
                                    String WeatherCountry = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("location").getString("country");
                                    String WeatherRegion = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("location").getString("region");

                                    ////// - units ///////
                                    String WeatherunitSpeed = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("units").getString("speed");
                                    String WeatherTemperature = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("units").getString("temperature");
                                    String WeatherPressure_unit = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("units").getString("pressure");
                                    String WeatherDistance_unit = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("units").getString("distance");

                                    ////// - wind ///////
                                    String WeatherDirection = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("wind").getString("direction");
                                    String Weatherwindspeed = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("wind").getString("speed");

                                    ////// - atmosphere ///////
                                    String WeatherHumidity = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("atmosphere").getString("humidity");
                                    String WeatherPressure = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("atmosphere").getString("pressure");
                                    String WeatherVisibility = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("atmosphere").getString("visibility");

                                    ////// - astronomy ///////
                                    String WeatherSunrise = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("astronomy").getString("sunrise");
                                    String WeatherSunset = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("astronomy").getString("sunset");

                                    ////// - condition ///////
                                    String WeatherCode = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("code");
                                    String Temperature = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("temp");
                                    String WeatherText = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("text");

                                    ////// - forecast ///////
                                    String WeatherForcast_code_day1 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day1(context, WeatherForcast_code_day1);
                                    String WeatherForcast_date_day1 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day1(context, WeatherForcast_date_day1);
                                    String WeatherForcast_day_day1 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day1(context, WeatherForcast_day_day1);
                                    String WeatherForcast_high_day1 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day1(context, WeatherForcast_high_day1);
                                    String WeatherForcast_low_day1 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day1(context, WeatherForcast_low_day1);


                                    String WeatherForcast_code_day2 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day2(context, WeatherForcast_code_day2);
                                    String WeatherForcast_date_day2 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day2(context, WeatherForcast_date_day2);
                                    String WeatherForcast_day_day2 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day2(context, WeatherForcast_day_day2);
                                    String WeatherForcast_high_day2 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day2(context, WeatherForcast_high_day2);
                                    String WeatherForcast_low_day2 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(1).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day2(context, WeatherForcast_low_day2);


                                    String WeatherForcast_code_day3 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day3(context, WeatherForcast_code_day3);
                                    String WeatherForcast_date_day3 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day3(context, WeatherForcast_date_day3);
                                    String WeatherForcast_day_day3 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day3(context, WeatherForcast_day_day3);
                                    String WeatherForcast_high_day3 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day3(context, WeatherForcast_high_day3);
                                    String WeatherForcast_low_day3 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(2).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day3(context, WeatherForcast_low_day3);

                                    String WeatherForcast_code_day4 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day4(context, WeatherForcast_code_day4);
                                    String WeatherForcast_date_day4 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day4(context, WeatherForcast_date_day4);
                                    String WeatherForcast_day_day4 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day4(context, WeatherForcast_day_day4);
                                    String WeatherForcast_high_day4 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day4(context, WeatherForcast_high_day4);
                                    String WeatherForcast_low_day4 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(3).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day4(context, WeatherForcast_low_day4);


                                    String WeatherForcast_code_day5 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day5(context, WeatherForcast_code_day5);
                                    String WeatherForcast_date_day5 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day5(context, WeatherForcast_date_day5);
                                    String WeatherForcast_day_day5 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day5(context, WeatherForcast_day_day5);
                                    String WeatherForcast_high_day5 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day5(context, WeatherForcast_high_day5);
                                    String WeatherForcast_low_day5 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(4).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day5(context, WeatherForcast_low_day5);


                                    String WeatherForcast_code_day6 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day6(context, WeatherForcast_code_day6);
                                    String WeatherForcast_date_day6 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day6(context, WeatherForcast_date_day6);
                                    String WeatherForcast_day_day6 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day6(context, WeatherForcast_day_day6);
                                    String WeatherForcast_high_day6 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day6(context, WeatherForcast_high_day6);
                                    String WeatherForcast_low_day6 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(5).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day6(context, WeatherForcast_low_day6);


                                    String WeatherForcast_code_day7 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day7(context, WeatherForcast_code_day7);
                                    String WeatherForcast_date_day7 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day7(context, WeatherForcast_date_day7);
                                    String WeatherForcast_day_day7 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day7(context, WeatherForcast_day_day7);
                                    String WeatherForcast_high_day7 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day7(context, WeatherForcast_high_day7);
                                    String WeatherForcast_low_day7 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(6).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day7(context, WeatherForcast_low_day7);


                                    String WeatherForcast_code_day8 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day8(context, WeatherForcast_code_day8);
                                    String WeatherForcast_date_day8 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day8(context, WeatherForcast_date_day8);
                                    String WeatherForcast_day_day8 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day8(context, WeatherForcast_day_day8);
                                    String WeatherForcast_high_day8 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day8(context, WeatherForcast_high_day8);
                                    String WeatherForcast_low_day8 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(7).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day8(context, WeatherForcast_low_day8);


                                    String WeatherForcast_code_day9 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day9(context, WeatherForcast_code_day9);
                                    String WeatherForcast_date_day9 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day9(context, WeatherForcast_date_day9);
                                    String WeatherForcast_day_day9 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day9(context, WeatherForcast_day_day9);
                                    String WeatherForcast_high_day9 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day9(context, WeatherForcast_high_day9);
                                    String WeatherForcast_low_day9 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(8).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day9(context, WeatherForcast_low_day9);


                                    String WeatherForcast_code_day10 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("code");
                                    ForecastConfig.store_WeatherForcast_code_day10(context, WeatherForcast_code_day10);
                                    String WeatherForcast_date_day10 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("date");
                                    ForecastConfig.store_WeatherForcast_date_day10(context, WeatherForcast_date_day10);
                                    String WeatherForcast_day_day10 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("day");
                                    ForecastConfig.store_WeatherForcast_day_day10(context, WeatherForcast_day_day10);
                                    String WeatherForcast_high_day10 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("high");
                                    ForecastConfig.store_WeatherForcast_high_day10(context, WeatherForcast_high_day10);
                                    String WeatherForcast_low_day10 = response.getJSONObject("query").getJSONObject("results")
                                            .getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(9).getString("low");
                                    ForecastConfig.store_WeatherForcast_low_day10(context, WeatherForcast_low_day10);

                                    ////// STORE DATA ///////

                                    Config.store_city_weather(context, WeatherCity);
                                    Config.store_country_weather(context, WeatherCountry);
                                    Config.store_region_weather(context, WeatherRegion);

                                    Config.store_speed_unit(context, WeatherunitSpeed);
                                    Config.store_temperature_unit(context, WeatherTemperature);
                                    Config.store_weather_temp_condition(context, Temperature);

                                    Config.store_wind_direction(context, WeatherDirection);
                                    Config.store_wind_speed(context, Weatherwindspeed);

                                    Config.store_humidity(context, WeatherHumidity);
                                    Config.store_pressure_unit(context, WeatherPressure_unit);
                                    Config.store_pressure(context, WeatherPressure);
                                    Config.store_visibility(context, WeatherVisibility);
                                    Config.store_distance_unit(context, WeatherDistance_unit);

                                    Config.store_sunrise(context, WeatherSunrise);
                                    Config.store_sunset(context, WeatherSunset);

                                    Config.store_weather_code(context, WeatherCode);
                                    Config.store_weather_temp_numb(context, Temperature);
                                    Config.store_weather_text_en(context, WeatherText);

                                    Config.firts_run(context);
                                    updateWeatherView();
                                } catch (Exception e) {

                                }
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                progressBat.dismiss();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Config.updateLoaded_false(context);
                                showSnackerror();
                                progressBat.dismiss();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Config.updateLoaded_false(context);
                                showSnackerror();
                                progressBat.dismiss();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Config.updateLoaded_false(context);
                                showSnackerror();
                                progressBat.dismiss();
                            }
                        });

            }else{
                showSnack();
            }
    }

    public static void showSnackRongCity(){

        Snackbar snackbar = Snackbar
                .make(slidingUpPanelLayout, "شهر مورد نظر یافت نشد !", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_icon));
        snackbar.show();
    }

    private void initSearchView(){
        CardView searchView = (CardView) findViewById(R.id.search_view);
        ImageView searchButton = (ImageView) findViewById(R.id.search_button);
        ImageView backArrow = (ImageView) findViewById(R.id.back_arrow);
        searchContent = (EditText) findViewById(R.id.serch_content);
        YoYo.with(Techniques.FadeOutLeft)
                .duration(0)
                .playOn(backArrow);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(section == searchOFF){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    section = searchON;
                    searchContent.setFocusableInTouchMode(true);
                    searchContent.requestFocus();
                    YoYo.with(Techniques.FadeOutLeft)
                            .duration(300)
                            .playOn(searchButton);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchButton.setImageResource(R.mipmap.ic_clean_text);
                            YoYo.with(Techniques.FadeInRight)
                                    .duration(200)
                                    .playOn(searchButton);
                        }
                    }, 300);
                    YoYo.with(Techniques.FadeInLeft)
                            .duration(300)
                            .playOn(backArrow);
                }else if(section == searchON){
                    searchContent.setText("");
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(section == searchON){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchContent.getWindowToken(), 0);

                    searchContent.setFocusable(false);
                    section = searchOFF;
                    YoYo.with(Techniques.FadeOutRight)
                            .duration(300)
                            .playOn(searchButton);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchButton.setImageResource(R.mipmap.ic_search_dark);
                            YoYo.with(Techniques.FadeInLeft)
                                    .duration(200)
                                    .playOn(searchButton);
                        }
                    }, 300);

                    YoYo.with(Techniques.FadeOutLeft)
                            .duration(300)
                            .playOn(backArrow);
                }
            }
        });

        searchContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(section == searchOFF){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    section = searchON;
                    searchContent.setFocusableInTouchMode(true);
                    searchContent.requestFocus();
                    YoYo.with(Techniques.FadeOutLeft)
                            .duration(300)
                            .playOn(searchButton);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            searchButton.setImageResource(R.mipmap.ic_clean_text);
                            YoYo.with(Techniques.FadeInRight)
                                    .duration(200)
                                    .playOn(searchButton);
                        }
                    }, 300);
                    YoYo.with(Techniques.FadeInLeft)
                            .duration(300)
                            .playOn(backArrow);
                }
            }
        });
        searchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    if(section == searchON){
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchContent.getWindowToken(), 0);

                        getCityWeatherInfo(searchContent.getText().toString());
                        searchContent.setFocusable(false);
                        section = searchOFF;
                        YoYo.with(Techniques.FadeOutRight)
                                .duration(300)
                                .playOn(searchButton);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                searchButton.setImageResource(R.mipmap.ic_search_dark);
                                YoYo.with(Techniques.FadeInLeft)
                                        .duration(200)
                                        .playOn(searchButton);
                            }
                        }, 300);

                        YoYo.with(Techniques.FadeOutLeft)
                                .duration(300)
                                .playOn(backArrow);
                    }else{

                    }
                    return true;
                }
                return false;
            }
        });
        FrameLayout degreeLayout = (FrameLayout) findViewById(R.id.degree_layout);
        TextView degreeView = (TextView) findViewById(R.id.degree_text);

        String inorder = Config.get_temperature_unit_inorder(context, "c");
        if(inorder == null){
            degreeView.setText((char) 0x00B0 + "C");
        }else{
            Log.e("temp_inorder", ""+inorder);
            if(inorder.equalsIgnoreCase("c")){
                degreeView.setText((char) 0x00B0 + "C");
            }else if(inorder.equalsIgnoreCase("F")){
                degreeView.setText((char) 0x00B0 + "F");
            }
        }
            temp_inorder = Config.get__weather_temp_condition(MainActivity.this, "c");
        degreeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] items = {
                        "سانتیگراد"+"  "+ "C" + (char) 0x00B0 ,
                        "فارنهایت"+"  " + "F" + (char) 0x00B0
                };
                new MaterialDialog.Builder(MainActivity.this)
                        .items(items)
                        .backgroundColor(getResources().getColor(R.color.dark_primary))
                        .itemsColor(getResources().getColor(R.color.white))
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                Log.e("which", ""+which);
                                switch (which){
                                    case 0:
                                        Config.store_temperature_unit_inorder(context,"c");
                                        initSearchView();
                                        break;
                                    case 1:
                                        Config.store_temperature_unit_inorder(context, "f");
                                        initSearchView();
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }
}
