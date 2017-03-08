package com.ukdev.smartbuzz.model;

import android.net.Uri;

public class RingtoneWrapper {

    private String title;
    private Uri uri;

    public RingtoneWrapper(String title, Uri uri) {
        this.title = title;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }

}
