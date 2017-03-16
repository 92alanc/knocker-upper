package com.ukdev.smartbuzz.frontend;

import android.app.Activity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Utils {

    public static void showAds(Activity activity, int resId) {
        AdView adView = (AdView) activity.findViewById(resId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

}
