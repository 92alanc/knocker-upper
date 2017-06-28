package com.ukdev.smartbuzz.listeners;

import android.view.View;

/**
 * Listener for recycler view item clicks
 *
 * @author Alan Camargo
 */
public interface RecyclerViewClickListener {

    /**
     * Method called when a recycler view item is clicked
     * @param view the clicked view
     * @param position the clicked position
     */
    void onItemClick(View view, int position);

}
