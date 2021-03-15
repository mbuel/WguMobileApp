package com.wgu.mbuel.wgumobileapp.app;

import android.app.Application;
import android.content.Context;

import com.wgu.mbuel.wgumobileapp.database.DBwguHelper;
import com.wgu.mbuel.wgumobileapp.database.WguDatabaseManager;

/**
 * Created by mbuel on 4/22/2017.
 */

public class App extends Application {
    private static Context context;
    private static DBwguHelper dBwguHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        dBwguHelper = new DBwguHelper();
        WguDatabaseManager.initializeInstance(dBwguHelper);
    }
    public static Context getContext(){return context;}
}
