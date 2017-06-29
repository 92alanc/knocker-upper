package com.ukdev.smartbuzz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.listeners.OnFragmentAttachListener;
import com.ukdev.smartbuzz.model.enums.Day;

/**
 * Fragment containing repetition information
 *
 * @author Alan Camargo
 */
public class RepetitionFragment extends Fragment {

    private Context context;
    private OnFragmentAttachListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        return inflater.inflate(R.layout.fragment_repetition, container, attachToRoot);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentAttachListener)
            listener = (OnFragmentAttachListener) context;
        else {
            String message = String.format("%s must implement the OnFragmentAttachedListener interface.",
                                           context.toString());
            throw new RuntimeException(message);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public Day[] getSelectedRepetition() {
        return null;
    }

}
