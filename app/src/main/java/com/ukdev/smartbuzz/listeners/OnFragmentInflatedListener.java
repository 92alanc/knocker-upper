package com.ukdev.smartbuzz.listeners;

import android.support.v4.app.Fragment;

/**
 * Listener for view inflation events
 *
 * @author Alan Camargo
 */
public interface OnFragmentInflatedListener {

    /**
     * Method called when a fragment is inflated
     * @param fragment the fragment
     */
    void onFragmentInflated(Fragment fragment);

}
