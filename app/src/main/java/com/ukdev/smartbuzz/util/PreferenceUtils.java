package com.ukdev.smartbuzz.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

    private Activity activity;
    private SharedPreferences preferences;

    /**
     * Default constructor for {@code PreferenceUtils}
     * @param activity the activity
     * @param preferences the preferences
     */
    public PreferenceUtils(Activity activity, SharedPreferences preferences) {
        this.activity = activity;
        this.preferences = preferences;
    }

    /**
     * Gets the theme
     * @return the theme
     */
    public Theme getTheme() {
        try {
            int defaultTheme = activity.getPackageManager()
                                       .getActivityInfo(activity.getComponentName(), 0)
                                       .getThemeResource();
            return Theme.valueOf(preferences.getInt(THEME, defaultTheme));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Theme.valueOf(preferences.getInt(THEME, Theme.LIGHT.getRes()));
        }
    }

    /**
     * Sets the theme
     * @param theme the theme
     */
    public void setTheme(Theme theme) {
        preferences.edit().putInt(THEME, theme.getRes()).apply();
    }

    /**
     * An application theme
     *
     * @author Alan Camargo
     */
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
