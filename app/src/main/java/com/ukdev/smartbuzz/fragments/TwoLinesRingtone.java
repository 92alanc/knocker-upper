package com.ukdev.smartbuzz.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.util.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment with a ringtone picker
 *
 * @author William Miranda
 */
public class TwoLinesRingtone extends TwoLinesDefaultFragment<Uri> {

    private static final int REQUEST_CODE_RINGTONE = 4;

    private Context context;
    private ViewGroup rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_lines_default, container, ATTACH_TO_ROOT);
        context = view.getContext();
        rootView = view.findViewById(R.id.rootView);
        if (value == null)
            value = RingtoneManager.getValidRingtoneUri(context);
        if (onFragmentInflatedListener != null)
            onFragmentInflatedListener.onViewInflated(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int volume = Utils.getDefaultVolume(context);
                final int flags = 0;
                if (audioManager != null)
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume, flags);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, summary);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, value);
                startActivityForResult(intent, REQUEST_CODE_RINGTONE);
            }
        });
        setValue(value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RINGTONE && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            setValue(uri);
        }
    }

    @Override
    public void setValue(Uri value) {
        this.value = value;
        if (textSummary != null) {
            boolean isDefaultAlarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                                                       .equals(value);
            if (value != null && !isDefaultAlarmUri) {
                Ringtone ringtone = RingtoneManager.getRingtone(context, value);
                if (ringtone != null) {
                    String ringtoneTitle = ringtone.getTitle(context);
                    textSummary.setText(ringtoneTitle);
                }
            } else
                textSummary.setText(R.string.ringtone_none);
            if (changeListener != null)
                changeListener.onChange(value);
        }
    }

}
