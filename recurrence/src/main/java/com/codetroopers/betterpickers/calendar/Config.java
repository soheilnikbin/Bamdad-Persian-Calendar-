package com.codetroopers.betterpickers.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by vMagnify on 3/31/16.
 */
public class Config {

    public static final String AppID = "dj0yJmk9eFdwUzBMaWl4TlhhJmQ9WVdrOVRVUjNOelZETkdVbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD01MQ--";
    public static final String Secret = "3bfef5ddea2b4c71d8b2c66df2dd926de6301ca3";
    public static final String ACCESS_KEY = "!JiKuLi&8$oMmOiLkji78o9o0UikJ7!";
    Context context;
    public static SharedPreferences prefs;
    public static Boolean FirstRun = false;
    public static Boolean isLoaded = false;
    public static Boolean FirstRunOfApp = false;

    public static Boolean animateMenu = true;


    public static boolean isNetworkConnected(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void firts_run(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("FirstRunPersianCalender",true);
        editor.commit();
        FirstRun = prefs.getBoolean("FirstRunPersianCalender", false);
    }
    public static void firts_run_of_app(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("FirstRunOfAppPersianCalender",true);
        editor.commit();
        FirstRunOfApp = prefs.getBoolean("FirstRunOfAppPersianCalender", false);
    }

    public static void updateLoaded_true(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("LoadPersianCalender",true);
        editor.commit();
        isLoaded = prefs.getBoolean("LoadPersianCalender", false);
    }

    public static void updateLoaded_false(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("LoadPersianCalender",false);
        editor.commit();
        isLoaded = prefs.getBoolean("LoadPersianCalender", false);
    }

    public static void init(Context context){
        try{
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            FirstRun = prefs.getBoolean("FirstRunPersianCalender", false);
            FirstRunOfApp = prefs.getBoolean("FirstRunOfAppPersianCalender", false);
            isLoaded = prefs.getBoolean("LoadPersianCalender", false);
        }catch(Exception e){
            Log.e("[NULL OBJECT]", "sharedPrefrence");
        }

    }
    public static void store_sunrise(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_sunrise", string).commit();
    }
    public static String  get_sunrise(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_sunrise", null);

        return string;
    }

    public static void store_sunset(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_sunset", string).commit();
    }
    public static String  get_sunset(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_sunset", null);

        return string;
    }

    public static void store_wind_direction(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_wind_direction", string).commit();
    }
    public static String  get_wind_direction(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_wind_direction", null);

        return string;
    }

    public static void store_wind_speed(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_wind_speed", string).commit();
    }
    public static String  get_wind_speed(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_wind_speed", null);

        return string;
    }

    public static void store_humidity(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_humidity", string).commit();
    }
    public static String  get_humidity(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_humidity", null);

        return string;
    }


    public static void store_pressure(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_pressure", string).commit();
    }
    public static String  get_pressure(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_pressure", null);

        return string;
    }

    public static void store_visibility(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_visibility", string).commit();
    }
    public static String  get_visibility(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_visibility", null);

        return string;
    }

    public static void store_distance_unit(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_distance_unit", string).commit();
    }
    public static String  get_distance_unit(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_distance_unit", null);

        return string;
    }


    public static void store_distance(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_distance", string).commit();
    }
    public static String  get_distance(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_distance", null);

        return string;
    }


    public static void store_pressure_unit(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_pressure_unit", string).commit();
    }
    public static String  get_pressure_unit(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_pressure_unit", null);

        return string;
    }

    public static void store_speed_unit(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_speed_unit", string).commit();
    }
    public static String  get_speed_unit(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_speed_unit", null);

        return string;
    }

    public static void store_temperature_unit(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_temperature_unit", string).commit();
    }
    public static String  get_temperature_unit(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_temperature_unit", null);

        return string;
    }

    public static void store_temperature_unit_inorder(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_temperature_unit_inorder", string).commit();
    }
    public static String  get_temperature_unit_inorder(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_temperature_unit_inorder", null);

        return string;
    }


    public static void store_city(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_city", string).commit();
    }
    public static String  get_city(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_city", null);

        return string;
    }



    public static void store_city_weather(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_city_weather", string).commit();
    }
    public static String  get_city_weather(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_city_weather", null);

        return string;
    }


    public static void store_country_weather(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_country_weather", string).commit();
    }
    public static String  get_country_weather(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_country_weather", null);

        return string;
    }

    public static void store_region_weather(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_region_weather", string).commit();
    }
    public static String  get_region_weather(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_region_weather", null);

        return string;
    }

    public static void store_weather_code(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_weather_code", string).commit();
    }
    public static String  get__weather_code(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_weather_code", null);

        return string;
    }

    public static void store_weather_temp_condition(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_weather_temp_condition", string).commit();
    }
    public static String  get__weather_temp_condition(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_weather_temp_condition", null);

        return string;
    }


    public static void store_weather_temp_numb(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_weather_temp_numb", string).commit();
    }
    public static String  get__weather_temp_numb(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_weather_temp_numb", null);

        return string;
    }


    public static void store_weather_text_en(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_weather_text_en", string).commit();
    }
    public static String  get__weather_text_en(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_weather_text_en", null);

        return string;
    }


    public static void store_weather_last_update(Context context, String string){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("store_weather_last_update", string).commit();
    }
    public static String  get__weather_last_update(Context context, String string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getString("store_weather_last_update", null);

        return string;
    }
}
