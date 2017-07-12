package com.ukdev.smartbuzz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.ukdev.smartbuzz.R;

/**
 * Fragment with a {@code Switch}
 *
 * @author William Miranda
 */
public class TwoLinesSwitch extends TwoLinesDefaultFragment<Boolean> {

    private CompoundButton mSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_lines_switch, container, ATTACH_TO_ROOT);
        mSwitch = (CompoundButton) view.findViewById(R.id.switch_view);
        setValue(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                setValue(isChecked);
            }
        });
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
        if (mSwitch != null)
            mSwitch.setChecked(value);
    }

}
