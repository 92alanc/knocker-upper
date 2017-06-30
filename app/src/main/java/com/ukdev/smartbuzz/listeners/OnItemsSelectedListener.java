package com.ukdev.smartbuzz.listeners;

/**
 * Listener for multi choice spinners
 *
 * @author Alan Camargo
 */
public interface OnItemsSelectedListener {

    /**
     * Method called when items are selected
     * in a {@link com.ukdev.smartbuzz.view.MultiChoiceSpinner}
     * @param selectionFlags the flags indicating which of
     *                       the items are selected and which
     *                       are not
     */
    void onItemsSelected(boolean[] selectionFlags);

}
