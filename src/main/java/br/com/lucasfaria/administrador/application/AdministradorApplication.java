package br.com.lucasfaria.administrador.application;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

public class AdministradorApplication extends Application {
    private static AdministradorApplication instance;
    private static Context appContext;

    public static AdministradorApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}