package com.byagowi.persiancalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by vMagnify on 6/12/16.
 */
public class Config {

    public static void store_moazen_singer(Context context, int id){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("store_moazen_singer", id).commit();
    }
    public static int  get_moazen_singer(Context context, int string){

        string = PreferenceManager.getDefaultSharedPreferences(context).getInt("store_moazen_singer", 0);

        return string;
    }
}
