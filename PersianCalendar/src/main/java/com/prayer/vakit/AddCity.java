/*
 * Copyright (c) 2016 Metin Kale
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prayer.vakit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.byagowi.persiancalendar.R;;
import com.prayer.App;
import com.prayer.BaseActivity;
import com.prayer.utils.PermissionUtils;
import com.prayer.vakit.times.CalcTimes;
import com.prayer.vakit.times.other.Cities;
import com.prayer.vakit.times.other.Cities.Item;
import com.prayer.vakit.times.other.Source;

import net.steamcrafted.materialiconlib.MaterialMenuInflater;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class AddCity extends BaseActivity implements OnItemClickListener, OnQueryTextListener, LocationListener, OnClickListener {
    private MyAdapter mAdapter;
    private FloatingActionButton mFab;
    private SearchView mSearchView;
    private MenuItem mSearchItem;
    public static TextView tvTop, tvDown;
    MaterialDialog updateAppDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vakit_addcity_prayer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary)));
        actionBar.setTitle("اضافه کردن شهر جدید");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
            window.setNavigationBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

        mFab = (FloatingActionButton) findViewById(R.id.search);
        mFab.setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(this);
        mAdapter = new MyAdapter(this);
        listView.setAdapter(mAdapter);

        TextView legacy = (TextView) findViewById(R.id.legacySwitch);
        legacy.setText(R.string.oldAddCity);
        legacy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(AddCity.this, AddCityLegacy.class));

            }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.get(this).pLocation) {
            checkLocation();
        }
    }

    public void checkLocation() {
        if (PermissionUtils.get(this).pLocation) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location loc = null;
            List<String> providers = lm.getProviders(true);

            if (loc != null)
                onLocationChanged(loc);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(false);
            criteria.setSpeedRequired(false);
            String provider = lm.getBestProvider(criteria, true);
            if (provider != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                lm.requestSingleUpdate(provider, this, null);
            }

        } else {
            PermissionUtils.get(this).needLocation(this);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mFab) {
            MenuItemCompat.collapseActionView(mSearchItem);
            MenuItemCompat.expandActionView(mSearchItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MaterialMenuInflater.with(this)
                .setDefaultColor(0xFFFFFFFF)
                .inflate(R.menu.search_prayer, menu);
        mSearchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        mSearchView.performClick();
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long index) {
        Cities.Item i = mAdapter.getItem(pos);
        switch (i.source) {
            case Calc:
                Bundle bdl = new Bundle();
                bdl.putString("city", i.city);
                bdl.putDouble("lat", i.lat);
                bdl.putDouble("lng", i.lng);
                CalcTimes.add(this, bdl);
                break;

        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        int status = NetworkUtil.getConnectivityStatusinteger(getBaseContext());
        if(status == 3){
            updateAppDialog = new MaterialDialog.Builder(this)
                    .title("دسترسی به اینترنت")
                    .content("جهت دریافت اوقات شرعی شهر " + query + "، باید اینترنت شما روشن باشد.")
                    .positiveText("باشه")
                    .onPositive((dialog, which) -> updateAppDialog.dismiss())
                    .show();
        }else{
            Cities.search(query, new Cities.Callback() {
                @Override
                public void onResult(List result) {
                    List<Item> items = result;
                    if ((items != null) && !items.isEmpty()) {
                        mAdapter.clear();
                        mAdapter.addAll(items);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }


    @Override
    public void onLocationChanged(Location loc) {
        if ((mAdapter.getCount() <= 1)) {
            mAdapter.clear();
            Item item = new Item();
            item.city = "GPS";
            item.country = "GPS";
            item.source = Source.Calc;
            item.lat = loc.getLatitude();
            item.lng = loc.getLongitude();
            mAdapter.add(item);
            mAdapter.notifyDataSetChanged();

            Cities.search(item.lat + "," + item.lng, new Cities.Callback() {
                @Override
                public void onResult(List result) {
                    List<Item> items = result;
                    if ((items != null) && !items.isEmpty()) {
                        mAdapter.clear();
                        mAdapter.addAll(items);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    static class MyAdapter extends ArrayAdapter<Cities.Item> {

        public MyAdapter(Context context) {
            super(context, 0, 0);

        }

        @Override
        public void addAll(Collection<? extends Item> collection) {
            super.addAll(collection);
            sort(new Comparator<Item>() {

                @Override
                public int compare(Item i0, Item i1) {
                    return i0.source.ordinal() - i1.source.ordinal();
                }

            });
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.vakit_addcity_row_prayer, null);
                vh = new ViewHolder();
                vh.city = (TextView) convertView.findViewById(R.id.city);
                vh.country = (TextView) convertView.findViewById(R.id.country);
                vh.sourcetxt = (TextView) convertView.findViewById(R.id.sourcetext);
                vh.source = (ImageView) convertView.findViewById(R.id.source);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            Cities.Item i = getItem(position);
            if (i.city != null && i.city.startsWith("BRUNSWICK")) {
                i.city = "Braunschweig"; //;)
            }
            vh.city.setText(i.city);
            vh.country.setText(i.country);

            vh.sourcetxt.setText(i.source.text);
            if (i.source.resId == 0) {
                vh.source.setVisibility(View.GONE);
            }
//            else {
//                vh.source.setImageResource(i.source.resId);
//                vh.source.setVisibility(View.VISIBLE);
//            }
            return convertView;
        }

        class ViewHolder {
            TextView country;
            TextView city;
            TextView sourcetxt;
            ImageView source;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        ///check gps and internet connection
        tvTop = (TextView) findViewById(R.id.toptext);
        tvDown = (TextView) findViewById(R.id.downtext);
        LocationManager lm = (LocationManager) App.getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            tvTop.setText(getResources().getString(R.string.off_gps_add_city));
            tvTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
        }else{
            tvTop.setText(getResources().getString(R.string.on_gps_add_city));
            checkLocation();
        }
        int status = NetworkUtil.getConnectivityStatusinteger(getBaseContext());
        if(status == 3){
            tvDown.setText(getResources().getString(R.string.on_net_add_city));
        }else{
            tvDown.setText("");
        }
    }
}
