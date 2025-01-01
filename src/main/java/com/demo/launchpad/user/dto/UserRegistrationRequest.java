package com.demo.launchpad.user.dto;

import java.util.List;

public record UserRegistrationRequest(String username, String email, String password, List<String> roles) {
}
