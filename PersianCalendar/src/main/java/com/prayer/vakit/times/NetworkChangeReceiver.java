package com.prayer.vakit.times;

/**
 * Created by vMagnify on 24/12/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Toast;

import com.byagowi.persiancalendar.R;
import com.byagowi.persiancalendar.view.activity.MainActivity;
import com.prayer.App;
import com.prayer.vakit.NetworkUtil;

import static com.prayer.vakit.AddCity.tvDown;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusinteger(context);


            if(status == 3){
                try {
                    tvDown.setText(App.getContext().getResources().getString(R.string.on_net_add_city));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    tvDown.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    }
}