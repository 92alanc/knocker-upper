package com.ukdev.smartbuzz.util;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

import com.ukdev.smartbuzz.R;

/**
 * Preferences utilities
 *
 * @author Alan Camargo
 */
public class PreferenceUtils {

    public static final String FILE_NAME = "smart_buzz_preferences";
    private static final String THEME = "theme";

    private SharedPreferences preferences;

    /**
     * Default constructor for {@code PreferenceUtils}
     * @param preferences the preferences
     */
    public PreferenceUtils(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public Theme getTheme() {
        return Theme.valueOf(preferences.getInt(THEME, Theme.DARK.getRes()));
    }

    public void setTheme(Theme theme) {
        preferences.edit().putInt(THEME, theme.getRes()).apply();
    }

    public enum Theme {

        DARK(R.style.Dark),
        LIGHT(R.style.Light);

        @StyleRes int res;

        Theme(@StyleRes int res) {
            this.res = res;
        }

        @StyleRes
        public int getRes() {
            return res;
        }

        @Nullable
        public static Theme valueOf(@StyleRes int res) {
            for (Theme theme : values()) {
                if (theme.getRes() == res)
                    return theme;
            }
            return null;
        }

    }

}
