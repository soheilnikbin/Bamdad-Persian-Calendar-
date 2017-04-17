package com.outlay;

import android.app.Application;
import android.content.Context;

import com.outlay.di.AppComponent;

import com.outlay.di.DaggerAppComponent;
import com.outlay.di.module.AppModule;
import com.outlay.di.module.DaoModule;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class App extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .daoModule(new DaoModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static AppComponent getComponent(Context context) {
        return ((App) context.getApplicationContext()).getAppComponent();
    }
}
