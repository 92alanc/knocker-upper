package com.ukdev.smartbuzz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.util.Utils;

/**
 * Fragment with a {@code SeekBar}
 *
 * @author William Miranda
 */
public class TwoLinesSeekBar extends TwoLinesDefaultFragment<Integer> {

    private SeekBar seekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_lines_seekbar, container, ATTACH_TO_ROOT);
        seekBar = view.findViewById(R.id.seekbar);
        seekBar.setMax(Utils.getMaxVolume(view.getContext()));
        if (value == null)
            value = Utils.getDefaultVolume(getContext());
        if (onFragmentInflatedListener != null)
            onFragmentInflatedListener.onViewInflated(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
        seekBar.setProgress(value);
    }

}
