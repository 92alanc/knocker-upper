package com.ukdev.smartbuzz.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnItemsSelectedListener;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.view.MultiChoiceSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment containing repetition information
 *
 * @author Alan Camargo
 */
public class RepetitionFragment extends Fragment implements OnItemsSelectedListener {

    private Day[] selectedRepetition;
    private OnItemsSelectedListener onItemsSelectedListener;

    /**
     * Default constructor for {@code RepetitionFragment}
     */
    public RepetitionFragment() {
        onItemsSelectedListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        return inflater.inflate(R.layout.fragment_repetition, container, attachToRoot);
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseComponents();
    }

    /**
     * Method called when items are selected
     * in a {@link MultiChoiceSpinner}
     * @param selectionFlags the flags indicating which of
     *                       the items are selected and which
     *                       are not
     */
    @Override
    public void onItemsSelected(boolean[] selectionFlags) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < selectionFlags.length; i++) {
            if (selectionFlags[i])
                indices.add(i);
        }
        selectedRepetition = new Day[indices.size()];
        for (int i = 0; i < indices.size(); i++)
            selectedRepetition[i] = Day.valueOf(indices.get(i));
    }

    /**
     * Gets the selected repetition
     * @return the selected repetition
     */
    public Day[] getSelectedRepetition() {
        return selectedRepetition;
    }

    private void initialiseComponents() {
        View view = getView();
        if (view != null) {
            MultiChoiceSpinner spinner = (MultiChoiceSpinner) view.findViewById(R.id.spinner_repetition);
            spinner.populate(R.array.days_of_the_week, onItemsSelectedListener);
        }
    }

}
