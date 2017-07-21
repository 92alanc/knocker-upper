package com.ukdev.smartbuzz.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.IdRes;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.misc.LogTool;

/**
 * View utilities
 *
 * @author Alan Camargo
 */
public class ViewUtils {

    /**
     * Determines whether the screen is locked
     * @param context the Android context
     * @return {@code true} if positive,
     *         otherwise {@code false}
     */
    public static boolean screenIsLocked(Context context) {
        KeyguardManager manager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return manager.inKeyguardRestrictedInputMode();
    }

    /**
     * Shows ads into an {@code AdView}
     * @param activity the target activity
     * @param resId the {@code AdView} resource ID
     */
    public static void showAds(Activity activity, @IdRes int resId) {
        AdView adView = activity.findViewById(resId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    /**
     * Shows app info
     * @param context the Android context
     */
    public static void showAppInfo(Context context) {
        LogTool log = new LogTool(context);
        try {
            PackageManager manager = context.getPackageManager();
            final int flags = 0;
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), flags);
            String version = info.versionName;
            String appName = context.getString(R.string.app_name);
            String about = context.getString(R.string.about);
            String text = String.format("%1$s %2$s\n%3$s", appName, version, about);
            log.info(text);
        } catch (PackageManager.NameNotFoundException e) {
            log.exception(e);
        }
    }

}
