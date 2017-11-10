package com.ukdev.smartbuzz.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.util.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment with a {@code ImagePicker}
 *
 * @author Alan Camargo
 */
public class TwoLinesImagePicker extends TwoLinesDefaultFragment<Uri>
        implements View.OnClickListener {

    private static final int REQUEST_CODE_IMAGE = 1;

    private ImageButton clearButton;
    private ImageView imageView;
    private ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_lines_image_picker, container, ATTACH_TO_ROOT);
        clearButton = view.findViewById(R.id.button_clear_wallpaper);
        clearButton.setOnClickListener(this);
        if (value == null)
            clearButton.setVisibility(View.GONE);
        else
            clearButton.setVisibility(View.VISIBLE);
        imageView = view.findViewById(R.id.image_wallpaper);
        rootView = view.findViewById(R.id.rootView);
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
                Utils.requestStoragePermission(getActivity());
                if (Utils.hasStoragePermission(getActivity())) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, REQUEST_CODE_IMAGE);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        startActivityForResult(intent, REQUEST_CODE_IMAGE);
                    }
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
        try {
            if (imageView != null) {
                imageView.setImageURI(value);
                if (value != null)
                    clearButton.setVisibility(View.VISIBLE);
                else
                    clearButton.setVisibility(View.GONE);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            // [WORKAROUND] This error only happens when some muppet keeps on opening and closing the fragment
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_clear_wallpaper)
            setValue(null);
    }

}
