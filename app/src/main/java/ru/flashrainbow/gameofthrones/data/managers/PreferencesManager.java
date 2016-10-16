package ru.flashrainbow.gameofthrones.data.managers;

import android.content.SharedPreferences;

import ru.flashrainbow.gameofthrones.utils.DevApplication;

/**
 * Сохранение/загрузка пользовательских данных, используя shared preferences
 */
public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    public PreferencesManager() {
        this.mSharedPreferences = DevApplication.getSharedPreferences();
    }
}
