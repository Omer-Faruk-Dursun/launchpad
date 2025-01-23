package com.demo.launchpad.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UserRegistrationRequest(@NotBlank String firstname, @NotBlank String lastname, @NotBlank String username, @NotBlank @Email String email, @NotBlank String password, List<String> roles) {
}
