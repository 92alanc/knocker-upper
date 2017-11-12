package com.ukdev.smartbuzz.util;

import android.content.SharedPreferences;

/**
 * Preferences utilities
 *
 * @author Alan Camargo
 */
public class PreferenceUtils {

    private static final String THEME = "theme";

    private SharedPreferences preferences;

    /**
     * Default constructor for {@code PreferenceUtils}
     * @param preferences the preferences
     */
    public PreferenceUtils(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void setTheme(Theme theme) {
        preferences.edit().putString(THEME, theme.name).apply();
    }

    public enum Theme {

        DARK("dark"),
        LIGHT("light");

        String name;

        Theme(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        private static String[] strings;

        static {
            for (int i = 0; i < values().length; i++)
                strings[i] = values()[i].name;
        }

        public static String[] getStrings() {
            return strings;
        }

    }

}
