package com.ukdev.smartbuzz.backend.enums;

public enum Extra {

    ID("ID");

    private final String value;

    Extra(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
