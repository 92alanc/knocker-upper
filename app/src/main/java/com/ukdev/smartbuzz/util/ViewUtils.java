package com.ukdev.smartbuzz.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.misc.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * View utilities
 *
 * @author Alan Camargo
 */
public class ViewUtils {

    /**
     * Populates the ringtone spinner
     * @param context the Android context
     * @param spinner the spinner
     */
    public static void populateRingtoneSpinner(Context context, AppCompatSpinner spinner) {
        List<Uri> ringtones = new ArrayList<>();
        ArrayAdapter<Uri> adapter = new ArrayAdapter<>(context,
                                                            android.R.layout.simple_spinner_item,
                                                            ringtones);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
    }

    /**
     * Populates the snooze duration spinner
     * @param context the Android context
     * @param spinner the spinner
     */
    public static void populateSnoozeDurationSpinner(Context context, AppCompatSpinner spinner) {
        String[] itemTexts = context.getResources().getStringArray(R.array.snooze_durations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                                                          android.R.layout.simple_spinner_item,
                                                          itemTexts);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
    }

    /**
     * Shows ads into an {@code AdView}
     * @param activity the target activity
     * @param resId the {@code AdView} resource ID
     */
    public static void showAds(Activity activity, @IdRes int resId) {
        AdView adView = (AdView) activity.findViewById(resId);
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
