package ru.flashrainbow.gameofthrones.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;

import org.greenrobot.greendao.database.Database;

import ru.flashrainbow.gameofthrones.data.storage.models.DaoMaster;
import ru.flashrainbow.gameofthrones.data.storage.models.DaoSession;

public class DevApplication extends Application {

    private static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static DaoSession sDaoSession;

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Инициализация БД
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "GameOfThrones-db");
        Database db = helper.getReadableDb();
        sDaoSession = new DaoMaster(db).newSession();

        // Подключение Stetho для дебага БД
        Stetho.initializeWithDefaults(this);
    }
}
