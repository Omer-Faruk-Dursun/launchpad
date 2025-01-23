package com.demo.launchpad.user.controller;

import com.demo.launchpad.user.dto.UserRegistrationRequest;
import com.demo.launchpad.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/user/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegistrationRequest registrationRequest) {
        service.saveUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}