package com.wgu.mbuel.wgumobileapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//HELPER CLASS to manage Database connection.

public class WguDatabaseManager {

    private Integer wguOpenCounter = 0;

    private static WguDatabaseManager instance;
    private static SQLiteOpenHelper wguDatabaseHelper;
    private SQLiteDatabase wguDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new WguDatabaseManager();
            wguDatabaseHelper = helper;
        }
    }

    public static synchronized WguDatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(WguDatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        wguOpenCounter +=1;
        if(wguOpenCounter == 1) {
            // Opening new database
            wguDatabase = wguDatabaseHelper.getWritableDatabase();
        }
        return wguDatabase;
    }

    public synchronized void closeDatabase() {
        wguOpenCounter -=1;
        if(wguOpenCounter == 0) {
            // Closing database
            wguDatabase.close();

        }
    }
}
