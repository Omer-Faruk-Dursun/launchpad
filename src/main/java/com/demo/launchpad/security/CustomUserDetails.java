package com.demo.launchpad.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class CustomUserDetails extends User {
    private final String firstname;
    private final String lastname;

    public CustomUserDetails(String username, String password, List<SimpleGrantedAuthority> authorities, String firstname, String lastname) {
        super(username, password, authorities);
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
} 