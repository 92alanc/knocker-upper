package com.ukdev.smartbuzz.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ukdev.smartbuzz.R;
import com.ukdev.smartbuzz.util.Utils;

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
                    if (Build.VERSION.SDK_INT < 19) {
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
        if (imageView != null && value != null)
            imageView.setImageURI(value);
        // FIXME: [KNOWN BUG] OutOfMemoryError when some muppet keeps on opening and closing this f***ing fragment
    }

}
