package com.example.klinikrizky;

import android.content.Context;
import android.content.SharedPreferences;

public class tools {
    private static final String PREFS_NAME = "TugasAkhir";

    public static String getSharedPreferenceString(Context c, String preference, String defaultValue) {
        try {
            if (c != null){
                SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
                return settings.getString(preference, defaultValue);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getSharedPreferenceInteger(Context c, String preference, int defaultValue) {
        try {
            if (c != null){
                SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
                return settings.getInt(preference, defaultValue);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getSharedPreferenceBoolean(Context c, String preference, boolean defaultValue) {
        try {
            if (c != null){
                SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
                return settings.getBoolean(preference, defaultValue);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void setSharedPreference(Context c, String preference, String prefValue) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(preference, prefValue);
        editor.apply();
    }

    public static void setSharedPreference(Context c, String preference, boolean prefValue) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(preference, prefValue);
        editor.apply();
    }

    public static void removeSharedPreference(Context c, String preference) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(preference);
        editor.apply();
    }

    public static void clearSharedPreference(Context c) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }
}
