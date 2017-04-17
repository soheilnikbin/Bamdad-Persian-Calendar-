package com.outlay.view.fragment;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.util.Utils;
import com.byagowi.persiancalendar.view.activity.ConvertPersianCal;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.outlay.App;
import com.outlay.adapter.ReportAdapter;
import com.outlay.helper.OnTabSelectedListenerAdapter;
import com.outlay.model.Report;
import com.outlay.presenter.ReportPresenter;
import com.outlay.utils.DateUtils;
import com.outlay.utils.ResourceUtils;
import com.outlay.view.dialog.DatePickerFragment;
import com.rey.material.widget.Slider;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import calendar.CivilDate;
import calendar.DateConverter;
import calendar.PersianDate;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class ReportFragment extends BaseFragment {
    public static final String ARG_DATE = "_argDate";

    public static final int PERIOD_DAY = 0;
    public static final int PERIOD_WEEK = 1;
    public static final int PERIOD_MONTH = 2;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Bind(R.id.range_view)
    LinearLayout rangeView;

    @Bind(R.id.main_range_view)
    LinearLayout MainRangeView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.noResults)
    View noResults;

    @Bind(R.id.range_slider)
    Slider rangeSlider;

    @Bind(R.id.renge_text_view)
    TextView rangeTextView;

    @Inject
    ReportPresenter presenter;

    int height;
    private int selectedPeriod;
    private Date selectedDate, startSelected, endSelected;
    private ReportAdapter adapter;
    public static String ExpensesListFragmentDate;
    private int animateView = 0;
    public static int mounthNumberRange , yearNumberRange ;
    CivilDate civilDate = null;
    private Utils utils;
    PersianDate persianDate, persianDateintro;
    int _a, _b, _c;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent(getActivity()).inject(this);
        presenter.attachView(this);
        selectedDate = new Date(getArguments().getLong(ARG_DATE, new Date().getTime()));
        utils = Utils.getInstance(getActivity());
        mounthNumberRange = 1;
        yearNumberRange = 1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_expense, null, false);
        ButterKnife.bind(this, view);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;

        YoYo.with(Techniques.FadeOut)
                .duration(0)
                .playOn(rangeView);
        MainRangeView.animate().setDuration(0).translationY(-(height/6));

        enableToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        updateTitle();
        rangeSlider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                String pNewValue = ConvertPersianCal.toPersianNumber(String.valueOf(newValue));
                switch (selectedPeriod){
                    case 0:
                        break;
                    case 1:
                        rangeTextView.setText(pNewValue + " ماه گذشته");
                        mounthNumberRange = newValue;
                        break;
                    case 2:
                        rangeTextView.setText(pNewValue + " سال گذشته");
                        yearNumberRange = newValue;
                        break;
                }
            }
        });

        rangeSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case 1:
                        updateTitle();
                        break;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_report, menu);
        MenuItem dateItem = menu.findItem(R.id.action_date);
        dateItem.setIcon(ResourceUtils.getMaterialToolbarIcon(getActivity(), R.string.ic_material_today));

        MenuItem listItem = menu.findItem(R.id.action_list);
        listItem.setIcon(ResourceUtils.getMaterialToolbarIcon(getActivity(), R.string.ic_material_list));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date:
                DatePickerDialog datePickerFragment = new DatePickerDialog();
                datePickerFragment.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
                    persianDate = new PersianDate(year, monthOfYear, dayOfMonth);
                    civilDate = DateConverter.persianToCivil(persianDate);

                    Calendar c = Calendar.getInstance();
                    c.set(civilDate.getYear(), civilDate.getMonth(), civilDate.getDayOfMonth());
                    Date selected = c.getTime();
                    selectedDate = selected;
                    ReportFragment.this.setTitle(DateUtils.toShortString(selected));
                     _a = year;
                     _b = monthOfYear+1;
                     _c = dayOfMonth;
                    updateTitle();

                });
                datePickerFragment.show(getChildFragmentManager(), "datePicker");
                datePickerFragment.setThemeDark(true);
                break;
            case R.id.action_list:
                presenter.goToExpensesList(selectedDate, selectedPeriod);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_day));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_week));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_month));
        tabLayout.getTabAt(selectedPeriod).select();
        tabLayout.setOnTabSelectedListener(new OnTabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedPeriod = tab.getPosition();
                updateTitle();
                switch (tab.getPosition()){
                    case 1:
                        rangeTextView.setText("۱ ماه گذشته");
                        rangeSlider.setValue(mounthNumberRange, true);
                        rangeSlider.setValueRange(1, 12, true);
                        mounthNumberRange = 1;
                        break;
                    case 2:
                        rangeTextView.setText("۱ سال گذشته");
                        rangeSlider.setValue(yearNumberRange, true);
                        rangeSlider.setValueRange(1, 30, true);
                        yearNumberRange = 1;
                        break;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReportAdapter();
        recyclerView.setAdapter(adapter);
        presenter.loadReport(selectedDate, selectedPeriod);

        adapter.setOnItemClickListener(report -> presenter.goToExpensesList(selectedDate, selectedPeriod, report.getCategory().getId()));
    }

    public void displayReports(List<Report> reportList) {
        if (reportList.isEmpty()) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);
            adapter.setItems(reportList);
        }
    }

    private void updateTitle() {
        switch (selectedPeriod) {
            case PERIOD_DAY:
                if(animateView == 1){
                    animateView = 0;
                    YoYo.with(Techniques.FadeOut)
                            .duration(300)
                            .playOn(rangeView);
                    MainRangeView.animate().setDuration(300).translationY(-(height/6));
                }
                Calendar _start_cal = Calendar.getInstance();
                _start_cal.setTime(selectedDate);
                CivilDate _start_cDate = new CivilDate(_start_cal.get(Calendar.YEAR), _start_cal.get(Calendar.MONTH)+1 , _start_cal.get(Calendar.DAY_OF_MONTH));
                PersianDate _start_pDate = null;
                try {
                    _start_pDate = new PersianDate(_a, _b, _c);
                } catch (Exception e) {
                    e.printStackTrace();
                    _start_pDate = DateConverter.civilToPersian(_start_cDate);
                }
                setTitle(utils.shape(utils.dateToString(_start_pDate)));
                ExpensesListFragmentDate = utils.shape(utils.dateToString(_start_pDate));
                presenter.loadReport(selectedDate, selectedPeriod);
                break;
            case PERIOD_WEEK:
                if(animateView == 0){
                    animateView = 1;
                    YoYo.with(Techniques.FadeIn)
                            .duration(300)
                            .playOn(rangeView);
                    MainRangeView.animate().setInterpolator(new AccelerateInterpolator()).setDuration(300).translationY(10);
                }
                Date startDate = selectedDate;
                Calendar lastThreeMonth = Calendar.getInstance();
                lastThreeMonth.setTime(selectedDate);
                lastThreeMonth.add(Calendar.MONTH, -mounthNumberRange);
                Date endDate = lastThreeMonth.getTime();

                Calendar start_cal = Calendar.getInstance();
                start_cal.setTime(startDate);
                CivilDate start_cDate = new CivilDate(start_cal.get(Calendar.YEAR), start_cal.get(Calendar.MONTH)+1 , start_cal.get(Calendar.DAY_OF_MONTH));
                PersianDate start_pDate = DateConverter.civilToPersian(start_cDate);

                Calendar end_cal = Calendar.getInstance();
                end_cal.setTime(endDate);
                CivilDate end_cDate = new CivilDate(end_cal.get(Calendar.YEAR), end_cal.get(Calendar.MONTH)+1 , end_cal.get(Calendar.DAY_OF_MONTH));
                PersianDate end_pDate = DateConverter.civilToPersian(end_cDate);

                setTitle(utils.shape(utils.dateToString(end_pDate)) + " تا " + utils.shape(utils.dateToString(start_pDate)));
                ExpensesListFragmentDate = utils.shape(utils.dateToString(end_pDate)) + " تا " + utils.shape(utils.dateToString(start_pDate));
                presenter.loadReport(selectedDate, selectedPeriod);
                break;
            case PERIOD_MONTH:
                if(animateView == 0){
                    animateView = 1;
                    YoYo.with(Techniques.FadeIn)
                            .duration(300)
                            .playOn(rangeView);
                    MainRangeView.animate().setInterpolator(new AccelerateInterpolator()).setDuration(300).translationY(10);
                }
                startDate = selectedDate;
                Calendar lastYear = Calendar.getInstance();
                lastYear.setTime(selectedDate);
                lastYear.add(Calendar.YEAR, -yearNumberRange);
                endDate = lastYear.getTime();

                Calendar start_cal_month = Calendar.getInstance();
                start_cal_month.setTime(startDate);
                CivilDate start_cDate_month = new CivilDate(start_cal_month.get(Calendar.YEAR), start_cal_month.get(Calendar.MONTH)+1 , start_cal_month.get(Calendar.DAY_OF_MONTH));
                PersianDate start_pDate_month = DateConverter.civilToPersian(start_cDate_month);

                Calendar end_cal_month = Calendar.getInstance();
                end_cal_month.setTime(endDate);
                CivilDate end_cDate_month = new CivilDate(end_cal_month.get(Calendar.YEAR), end_cal_month.get(Calendar.MONTH)+1 , end_cal_month.get(Calendar.DAY_OF_MONTH));
                PersianDate end_pDate_month = DateConverter.civilToPersian(end_cDate_month);
                setTitle(utils.shape(utils.dateToString(end_pDate_month)) + " تا " + utils.shape(utils.dateToString(start_pDate_month)));
                ExpensesListFragmentDate = utils.shape(utils.dateToString(end_pDate_month)) + " تا " + utils.shape(utils.dateToString(start_pDate_month));
                presenter.loadReport(selectedDate, selectedPeriod);
                break;
        }
    }
}