package com.outlay.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.adapter.DrawerAdapterRecurrence;
import com.byagowi.persiancalendar.util.Utils;
import com.byagowi.persiancalendar.view.activity.ConvertPersianCal;
import com.github.johnkil.print.PrintView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.omninotes.MainActivityPep;
import com.outlay.Constants;
import com.outlay.dao.Category;
import com.outlay.model.Summary;
import com.outlay.utils.IconUtils;
import com.outlay.view.alert.Alert;
import com.outlay.view.fragment.AboutFragment;
import com.outlay.view.fragment.CategoriesFragment;
import com.recurrence.activities.*;
import com.recurrence.activities.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class BaseActivity extends AppCompatActivity {

    private static final int ITEM_CATEGORIES = 1;
    private static final int ITEM_CALENDAR = 2;
    private static final int ITEM_REMINDER = 3;
    private static final int ITEM_NOTE = 4;


    private Drawer drawer;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fragment)
    FrameLayout appMainView;


    ImageView imageView;

    private View headerView;
    private LayoutInflater inflater;
    private Utils utils;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (hasDrawer()) {
            setupDrawer(this.toolbar);
        }
        inflater = LayoutInflater.from(this);
    }

    public void setupDrawer(Toolbar toolbar) {
        LayoutInflater inflater = LayoutInflater.from(this);
        headerView = inflater.inflate(R.layout.layout_drawer_header_expense, null, false);

        imageView = (ImageView) headerView.findViewById(R.id.image_drawer_expense);
        drawer = new DrawerBuilder()
                .withHeader(headerView)
                .withToolbar(toolbar)
                .withFullscreen(true)
                .withActivity(this)
                .withSelectedItem(-1)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.menu_item_categories).withIcon(GoogleMaterial.Icon.gmd_apps).withIdentifier(ITEM_CATEGORIES),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("تقویم").withIcon(R.drawable.ic_date_range_money).withIdentifier(ITEM_CALENDAR),
                        new PrimaryDrawerItem().withName("یادآور و ثبت رویداد").withIcon(R.drawable.ic_roydad_money).withIdentifier(ITEM_REMINDER),
                        new PrimaryDrawerItem().withName("یادداشت").withIcon(R.drawable.ic_note_money).withIdentifier(ITEM_NOTE)

                )
                .withOnDrawerItemClickListener((view, i, iDrawerItem) -> {
                    if (iDrawerItem != null) {
                        int id = iDrawerItem.getIdentifier();
                        switch (id) {
                            case ITEM_CATEGORIES:
                                SingleFragmentActivity.start(BaseActivity.this, CategoriesFragment.class);
                                drawer.closeDrawer();
                                break;
                            case ITEM_CALENDAR:
                                startActivity(new Intent(getApplicationContext(), com.byagowi.persiancalendar.view.activity.MainActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                                break;
                            case ITEM_REMINDER:
                                startActivity(new Intent(getApplicationContext(), com.recurrence.activities.MainActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                                break;
                            case ITEM_NOTE:
                                startActivity(new Intent(getApplicationContext(), MainActivityPep.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                                break;
                        }
                        drawer.setSelection(-1);
                    }
                    return false;
                })
                .build();
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer.getDrawerLayout(), toolbar, R.string.openDrawer, R.string.closeDrawer) {

            int slidingDirection = +1;


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
                drawer.getDrawerLayout().bringChildToFront(drawerView);
                drawer.getDrawerLayout().requestLayout();
            }
        };
        drawer.getDrawerLayout().setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        utils = Utils.getInstance(getBaseContext());
        switch (utils.getSeason()) {
            case SPRING:
                imageView.setImageResource(R.drawable.spring);
                break;

            case SUMMER:
                imageView.setImageResource(R.drawable.summer);
                break;

            case FALL:
                imageView.setImageResource(R.drawable.fall);
                break;

            case WINTER:
                imageView.setImageResource(R.drawable.winter);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void updateDrawerData(Summary summary) {
        if (headerView != null) {
            TextView dateAmount = (TextView) headerView.findViewById(R.id.dateAmount);
            TextView weekAmount = (TextView) headerView.findViewById(R.id.weekAmount);
            TextView monthAmount = (TextView) headerView.findViewById(R.id.monthAmount);

            dateAmount.setText(ConvertPersianCal.toPersianNumber(String.valueOf(summary.getDayAmount())));
            weekAmount.setText(ConvertPersianCal.toPersianNumber(String.valueOf(summary.getWeekAmount())));
            monthAmount.setText(ConvertPersianCal.toPersianNumber(String.valueOf(summary.getMonthAmount())));

            LinearLayout categoriesContainer = (LinearLayout) headerView.findViewById(R.id.mostSpentCategories);
            categoriesContainer.removeAllViews();
            for (Category c : summary.getCategories()) {
                categoriesContainer.addView(createCategoryView(c));
            }
        }
    }

    public boolean hasDrawer() {
        return true;
    }

    public Drawer getDrawer() {
        return drawer;
    }

    private View createCategoryView(Category category) {
        View categoryView = inflater.inflate(R.layout.item_category_expense, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1);
        categoryView.setLayoutParams(layoutParams);

        PrintView icon = (PrintView) categoryView.findViewById(R.id.categoryIcon);
        TextView title = (TextView) categoryView.findViewById(R.id.categoryTitle);
        title.setText(category.getTitle());
        icon.setIconSizeRes(R.dimen.category_icon);
        IconUtils.loadCategoryIcon(category, icon);
        return categoryView;
    }

    public View getRootView() {
        return findViewById(android.R.id.content);
    }
}
