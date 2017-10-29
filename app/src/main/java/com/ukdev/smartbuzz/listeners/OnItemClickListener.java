package com.ukdev.smartbuzz.listeners;

/**
 * Listener for recycler view item clicks
 *
 * @author Alan Camargo
 */
public interface OnItemClickListener {

    /**
     * Method called when a recycler view item is clicked
     * @param position the clicked position
     */
    void onItemClick(int position);

}
