package com.ukdev.smartbuzz.frontend;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * General frontend utilities
 *
 * @author Alan Camargo
 */
public class Utils {

    /**
     * Shows ads into an {@link AdView}
     * @param activity the target activity
     * @param resId the {@link AdView} resource ID
     */
    public static void showAds(Activity activity, @LayoutRes int resId) {
        AdView adView = (AdView) activity.findViewById(resId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

}
