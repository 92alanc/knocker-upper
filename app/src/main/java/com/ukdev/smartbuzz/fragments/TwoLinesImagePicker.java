package com.ukdev.smartbuzz.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.misc.LogTool;
import com.ukdev.smartbuzz.util.Utils;

import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment with a {@code ImagePicker}
 *
 * @author Alan Camargo
 */
public class TwoLinesImagePicker extends TwoLinesDefaultFragment<Uri> {

    private static final int REQUEST_CODE_IMAGE = 1;

    private ImageView imageView;
    private ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_lines_image_picker, container, ATTACH_TO_ROOT);
        imageView = view.findViewById(R.id.image_wallpaper);
        rootView = view.findViewById(R.id.rootView);
        if (onViewInflatedListener != null)
            onViewInflatedListener.onViewInflated(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.requestStoragePermission(getActivity());
                if (Utils.hasStoragePermission(getActivity())) {
                    Intent getContentIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getContentIntent.setType("image/*");
                    Intent pickIntent = new Intent(Intent.ACTION_PICK,
                                                   MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    String dialogueTitle = getContext().getString(R.string.select_image);
                    Intent chooserIntent = Intent.createChooser(getContentIntent, dialogueTitle);
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                    startActivityForResult(chooserIntent, REQUEST_CODE_IMAGE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK)
            setValue(data.getData());
    }

    @Override
    public void setValue(Uri value) {
        this.value = value;
        if (imageView != null && value != null) {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(value);
                Drawable background = Drawable.createFromStream(inputStream, value.toString());
                imageView.setBackground(background);
            } catch (IOException e) {
                LogTool logTool = new LogTool(getContext());
                logTool.exception(e);
            }
        }
    }

}
