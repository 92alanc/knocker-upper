package com.ukdev.smartbuzz.listeners;

/**
 * Listener for fragment attachment events
 *
 * @author Alan Camargo
 */
public interface OnFragmentAttachListener {

    /**
     * Method called when a fragment starts loading
     */
    void onLoadFragment();

    /**
     * Method called when a fragment is fully attached
     */
    void onAttachFragment();

}
