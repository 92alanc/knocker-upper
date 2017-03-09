package com.ukdev.smartbuzz.backend.enums;

public enum Extra {

    ID("ID"),
    SLEEP_CHECKER_ON("SLEEP_CHECKER_ON");

    private final String value;

    Extra(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
