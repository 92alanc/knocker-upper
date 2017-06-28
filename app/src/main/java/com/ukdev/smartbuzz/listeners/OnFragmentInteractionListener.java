package com.ukdev.smartbuzz.listeners;

import android.net.Uri;

/**
 * Listener for fragment-activity interactions
 *
 * @author Alan Camargo
 */
public interface OnFragmentInteractionListener {

    /**
     * Method called when there is an interaction
     * between a fragment and an activity
     * @param uri the URI
     */
    void onFragmentInteraction(Uri uri);

}
