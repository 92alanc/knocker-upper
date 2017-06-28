package com.ukdev.smartbuzz.util;

import android.app.Activity;
import android.support.annotation.IdRes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * View utilities
 *
 * @author Alan Camargo
 */
public class ViewUtils {

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
