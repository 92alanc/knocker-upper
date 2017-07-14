package com.ukdev.smartbuzz.listeners;

import android.support.v4.app.Fragment;

/**
 * Listener for view inflation events
 *
 * @author Alan Camargo
 */
public interface OnViewInflatedListener {

    /**
     * Method called when a view is inflated
     * @param fragment the fragment
     */
    void onViewInflated(Fragment fragment);

}
