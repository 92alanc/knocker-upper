package com.ukdev.smartbuzz.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.util.PreferenceUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Fragment containing a {@code RadioGroup}
 *
 * @author Alan Camargo
 */
public class TwoLinesThemePicker extends TwoLinesDefaultFragment<PreferenceUtils.Theme> {

    private static final String ARG_OPTIONS_TEXT = "options_text";
    private static final String ARG_OPTIONS_VALUE = "options_value";

    public static TwoLinesThemePicker newInstance(String[] labels,
                                                  PreferenceUtils.Theme[] values) {
        TwoLinesThemePicker instance = new TwoLinesThemePicker();
        Bundle args = new Bundle();
        args.putStringArray(ARG_OPTIONS_TEXT, labels);
        args.putSerializable(ARG_OPTIONS_VALUE, values);
        instance.setArguments(args);
        return instance;
    }

    private ViewGroup rootView;
    private Map<PreferenceUtils.Theme, String> mapOptionValue;
    private int selectedIndex = -1;
    private String[] labels;
    private PreferenceUtils.Theme[] values;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_lines_default, container, ATTACH_TO_ROOT);
        rootView = view.findViewById(R.id.rootView);
        if (getArguments() != null) {
            parseArgs();
            mapOptionValue = new LinkedHashMap<>();
            for (int i = 0; i < values.length; i++)
                mapOptionValue.put(values[i], labels[i]);
        }
        if (onFragmentInflatedListener != null)
            onFragmentInflatedListener.onFragmentInflated(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogue(view.getContext());
            }
        });
    }

    @Override
    protected void setValue(PreferenceUtils.Theme value) {
        this.value = value;
        if (textSummary != null)
            textSummary.setText(mapOptionValue.get(value));
    }

    @Override
    public PreferenceUtils.Theme getValue() {
        if (value == null)
            value = PreferenceUtils.Theme.DARK;
        return value;
    }

    public void setDefaultSelectedItem() {
        Context context = getContext();
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(PreferenceUtils.FILE_NAME,
                                                                         Context.MODE_PRIVATE);
            PreferenceUtils preferenceUtils = new PreferenceUtils(preferences);
            setValue(preferenceUtils.getTheme());
        }
    }

    private final DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogueInterface, int which) {
            selectedIndex = which;
        }
    };

    private void parseArgs() {
        Bundle args = getArguments();
        assert args != null;
        labels = args.getStringArray(ARG_OPTIONS_TEXT);
        values = (PreferenceUtils.Theme[]) args.getSerializable(ARG_OPTIONS_VALUE);
    }

    private void showDialogue(Context context) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(context);
        dialogueBuilder.setTitle(title);
        dialogueBuilder.setSingleChoiceItems(mapOptionValue.values().toArray(new String[0]),
                                             selectedIndex, clickListener);

        dialogueBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogueInterface, int which) {
                int i = 0;
                for (Map.Entry<PreferenceUtils.Theme, String> entry : mapOptionValue.entrySet()) {
                    if (i == selectedIndex)
                        setValue(entry.getKey());
                    i++;
                }
                if (changeListener != null)
                    changeListener.onChange(value);
            }
        });
        dialogueBuilder.setNegativeButton(R.string.cancel, null);
        dialogueBuilder.show();
    }

}
