package com.pronchenko.top.soilcare3.entity;

import lombok.Getter;

@Getter
public enum ElementType {
    MACRO_NUTRIENT("Макроэлемент"),
    MICRO_NUTRIENT("Микроэлемент"),
    PH_LEVEL("Уровень pH"),
    ORGANIC_MATTER("Органическое вещество"),
    OTHER("Другое");
    private final String displayName;

    ElementType(String displayName) {
        this.displayName = displayName;
    }
}
