package com.ukdev.smartbuzz.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ukdev.smartbuzz.model.Ringtone;

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
        List<Ringtone> ringtones = Ringtone.getAllRingtones(context);
        ArrayAdapter<Ringtone> adapter = new ArrayAdapter<>(context,
                                                            android.R.layout.simple_spinner_item,
                                                            ringtones);
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

}
