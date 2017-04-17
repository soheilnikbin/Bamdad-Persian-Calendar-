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

package com.compass.compass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.view.activity.MainActivity;
import com.compass.App;
import com.compass.Utils;
import com.compass.compass._2D.Frag2D;
import com.compass.compass._3D.Frag3D;
import com.compass.compass.classes.OrientationCalculator;
import com.compass.compass.classes.OrientationCalculatorImpl;
import com.compass.compass.classes.math.Matrix4;
import com.compass.compass.classes.rotation.MagAccelListener;
import com.compass.compass.classes.rotation.RotationUpdateDelegate;
import com.compass.settings.Prefs;
import com.compass.utils.PermissionUtils;

import java.util.List;

public class Main extends AppCompatActivity implements LocationListener, RotationUpdateDelegate {

    private static double mQAngle;
    private static float mDist;
    public MagAccelListener mMagAccel;
    private Matrix4 mRotationMatrix = new Matrix4();
    private int mDisplayRotation;
    private SensorManager mSensorManager;
    private TextView mSelCity;
    private MenuItem mRefresh;
    private MenuItem mSwitch;
    private boolean mOnlyNew;
    private MyCompassListener mList;
    private OrientationCalculator mOrientationCalculator = new OrientationCalculatorImpl();
    private float[] mDerivedDeviceOrientation = {0, 0, 0};
    private Frag2D mFrag2D;
    private Frag3D mFrag3D;
    private Mode mMode;
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    public static void setContext(Context context) {
        sContext = context;
    }

    enum Mode {
        TwoDim,
        ThreeDim,
        Map,
    }

    public static float getDistance() {
        return mDist;
    }

    public static double getQiblaAngle() {
        return mQAngle;
    }

    private static final String TAG = "CompassActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass_main_compass);
        setContext(getBaseContext());
        Utils.changeLanguage("en");
        PermissionUtils.get(this).needLocation(this);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("قطب نما");
        toolbar.setSubtitle("قبله نما");


        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mDisplayRotation = display.getRotation();

        // sensor listeners
        mMagAccel = new MagAccelListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mFrag2D = new Frag2D();
        mList = mFrag2D;
        fragmentTransaction.add(R.id.frag2D, mFrag2D, "2d");
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFrag(Mode mode) {


        if (mMode != mode) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if ((mode == Mode.TwoDim) && mFrag2D.mHidden) {
                fragmentTransaction.remove((Fragment) mList);
                mList = mFrag2D;
                mFrag2D.show();
            } else if (mode == Mode.ThreeDim) {

                if (PermissionUtils.get(this).pCamera) {

                    if (mFrag3D == null) {
                        mFrag3D = new Frag3D();
                    }

                    if (mList != mFrag3D) {
                        fragmentTransaction.replace(R.id.frag, mFrag3D, "3d");

                        mList = mFrag3D;
                        mFrag2D.hide();
                    }
                } else {
                    PermissionUtils.get(this).needCamera(this);
                }

            }

            fragmentTransaction.commit();
        }
        mMode = mode;
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.get(this).pLocation) {
            LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            List<String> providers = locMan.getProviders(true);
            for (String provider : providers) {
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
                locMan.requestLocationUpdates(provider, 0, 0, this);
                Location lastKnownLocation = locMan.getLastKnownLocation(provider);
                if (lastKnownLocation != null) {
                    calcQiblaAngel(lastKnownLocation);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.unregisterListener(mMagAccel);

        mSensorManager.registerListener(mMagAccel, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mMagAccel, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);

        if (mSelCity == null) {
            mSelCity = (TextView) findViewById(R.id.selcity);
            mSelCity.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (App.isOnline()) {
                        startActivity(new Intent(Main.this, LocationPicker.class));
                    } else {
                        Toast.makeText(Main.this, R.string.noConnection, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if (PermissionUtils.get(this).pLocation) {
            LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            List<String> providers = locMan.getProviders(true);
            for (String provider : providers) {
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
                locMan.requestLocationUpdates(provider, 0, 0, this);
                Location lastKnownLocation = locMan.getLastKnownLocation(provider);
                if (lastKnownLocation != null) {
                    calcQiblaAngel(lastKnownLocation);
                }
            }
        }

        if (Prefs.getCompassLat() != 0) {
            Location loc = new Location("custom");
            loc.setLatitude(Prefs.getCompassLat());
            loc.setLongitude(Prefs.getCompassLng());
            calcQiblaAngel(loc);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (PermissionUtils.get(this).pCamera) {
//            mMode = Mode.TwoDim;
//        }
//    }

    // RotationUpdateDelegate methods
    @Override
    public void onRotationUpdate(float[] newMatrix) {
        if (mMode == Mode.Map) {
            return;
        }
        // remap matrix values according to display rotation, as in
        // SensorManager documentation.
        switch (mDisplayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(newMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, newMatrix);
                break;
            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(newMatrix, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, newMatrix);
                break;
            default:
                break;
        }
        mRotationMatrix.set(newMatrix);
        mOrientationCalculator.getOrientation(mRotationMatrix, mDisplayRotation, mDerivedDeviceOrientation);

        updateFrag((mDerivedDeviceOrientation[1] > -55f) ? Mode.ThreeDim : Mode.TwoDim);

        mList.onUpdateSensors(mDerivedDeviceOrientation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mMagAccel);
        if (PermissionUtils.get(this).pLocation) {
            LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locMan.removeUpdates(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if ((System.currentTimeMillis() - location.getTime()) < (mOnlyNew ? (1000 * 60) : (1000 * 60 * 60 * 24))) {
            LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locMan.removeUpdates(this);
        }

    }

    private void calcQiblaAngel(Location location) {
        if (location.getProvider() != "custom") {
            mSelCity.setVisibility(View.GONE);
        }
        double lat1 = location.getLatitude();// Latitude of Desired Location
        double lng1 = location.getLongitude();// Longitude of Desired Location
        double lat2 = 21.42247;// Latitude of Mecca (+21.45° north of Equator)
        double lng2 = 39.826198;// Longitude of Mecca (-39.75° east of Prime
        // Meridian)

        double q = -getDirection(lat1, lng1, lat2, lng2);

        Location mLoc = new Location(location);
        mLoc.setLatitude(lat2);
        mLoc.setLongitude(lng2);
        mQAngle = q;
        mDist = location.distanceTo(mLoc) / 1000;
        mList.onUpdateDirection();

    }

    private double getDirection(double lat1, double lng1, double lat2, double lng2) {
        double dLng = lng1 - lng2;
        return Math.toDegrees(getDirectionRad(Math.toRadians(lat1), Math.toRadians(lat2), Math.toRadians(dLng)));
    }

    private double getDirectionRad(double lat1, double lat2, double dLng) {
        return Math.atan2(Math.sin(dLng), (Math.cos(lat1) * Math.tan(lat2)) - (Math.sin(lat1) * Math.cos(dLng)));
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

    public interface MyCompassListener {
        void onUpdateDirection();

        void onUpdateSensors(float[] rot);
    }
    int getTopMargin() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            return getStatusBarHeight();
        }
        return 0;
    }

    public int getBottomMargin() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            return getNavBarHeight();
        }
        return 0;
    }
    int getStatusBarHeight() {

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
    int getNavBarHeight() {
        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    PermissionUtils.get(this).pCamera = true;
                } else {
                    PermissionUtils.get(this).pCamera = false;
                }
                return;

            case 4:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    PermissionUtils.get(this).pLocation = true;
                } else {
                    PermissionUtils.get(this).pLocation = false;
                    Prefs.setCalendar("-1");
                }
                return;
        }
    }
}
