package com.pronchenko.top.soilcare3.entity;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, SELLER;


    @Override
    public String getAuthority() {return name();}
}
