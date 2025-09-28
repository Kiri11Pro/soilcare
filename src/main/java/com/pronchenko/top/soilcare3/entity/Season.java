package com.pronchenko.top.soilcare3.entity;

import lombok.Getter;

@Getter
public enum Season {
    SPRING("Весна"),
    SUMMER("Лето"),
    AUTUMN("Осень"),
    WINTER("Зима"),
    ALL_SEASON("Круглый год");

    private final String displayName;

    Season(String displayName) {
        this.displayName = displayName;
    }

}
