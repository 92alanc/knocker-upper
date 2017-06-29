package com.ukdev.smartbuzz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.database.AlarmDao;
import com.ukdev.smartbuzz.listeners.OnFragmentAttachListener;
import com.ukdev.smartbuzz.misc.IntentExtra;
import com.ukdev.smartbuzz.model.Alarm;
import com.ukdev.smartbuzz.model.enums.Day;
import com.ukdev.smartbuzz.util.ViewUtils;

/**
 * Fragment containing repetition information
 *
 * @author Alan Camargo
 */
public class RepetitionFragment extends Fragment {

    private AppCompatSpinner spinner;
    private Context context;
    private OnFragmentAttachListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final boolean attachToRoot = false;
        return inflater.inflate(R.layout.fragment_repetition, container, attachToRoot);
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onLoadFragment();
        initialiseComponents();
        listener.onAttachFragment();
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

    /**
     * Gets the selected repetition
     * @return the selected repetition
     */
    public Day[] getSelectedRepetition() {
        // TODO: implement getSelectedRepetition
        throw new UnsupportedOperationException();
    }

    private void initialiseComponents() {
        context = getContext();
        boolean editMode = false;
        if (getArguments() != null)
            editMode = getArguments().getBoolean(IntentExtra.EDIT_MODE.toString());
        View view = getView();
        if (view != null) {
            spinner = (AppCompatSpinner) view.findViewById(R.id.spinner_repetition);
            Day[] repetition;
            if (editMode) {
                int id = getArguments().getInt(IntentExtra.ID.toString());
                AlarmDao dao = AlarmDao.getInstance(context);
                Alarm alarm = dao.select(id);
                repetition = alarm.getRepetition();
            } else
                repetition = Day.values();
            ViewUtils.populateRepetitionSpinner(context, repetition, spinner);
        }
    }

}
