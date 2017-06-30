package com.ukdev.smartbuzz.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ArrayRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnItemsSelectedListener;

/**
 * Multi choice spinner
 *
 * @author Alan Camargo
 */
public class MultiChoiceSpinner extends AppCompatSpinner
        implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private boolean[] selected;
    private CharSequence[] itemLabels;
    private OnItemsSelectedListener onItemsSelectedListener;

    /**
     * Default constructor for {@code MultiChoiceSpinner}
     * @param context the Android context
     */
    public MultiChoiceSpinner(Context context) {
        super(context);
    }

    /**
     * Inherited constructor
     * @param context the Android context
     * @param attributeSet the attribute set
     */
    public MultiChoiceSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * Inherited constructor
     * @param context the Android context
     * @param attributeSet the attribute set
     * @param style the style
     */
    public MultiChoiceSpinner(Context context, AttributeSet attributeSet, @StyleRes int style) {
        super(context, attributeSet, style);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        onItemsSelectedListener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener = this;
        DialogInterface.OnCancelListener onCancelListener = this;
        builder.setMultiChoiceItems(itemLabels, selected, onMultiChoiceClickListener);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setOnCancelListener(onCancelListener);
        builder.show();
        return true;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
        selected[position] = isChecked;
    }

    /**
     * Populates the spinner
     * @param itemTexts the resource ID for the array containing
     *                  the text for each spinner item
     * @param onItemsSelectedListener the onItemsSelectedListener for the items selected
     */
    public void populate(@ArrayRes int itemTexts, OnItemsSelectedListener onItemsSelectedListener) {
        this.onItemsSelectedListener = onItemsSelectedListener;
        itemLabels = getContext().getResources().getStringArray(itemTexts);
        selected = new boolean[itemLabels.length];
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(),
                                                                android.R.layout.simple_spinner_item,
                                                                itemLabels);
        setAdapter(adapter);
    }

}
