package com.pronchenko.top.soilcare3.entity;

import lombok.Getter;

@Getter
public enum FertilizerType {
    ORGANIC("Органическое"),
    MINERAL("Минеральное"),
    ORGANO_MINERAL("Органоминеральное");
    private final String displayName;

    FertilizerType(String displayName) {
        this.displayName = displayName;
    }
}

