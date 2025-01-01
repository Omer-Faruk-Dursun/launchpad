package com.demo.launchpad.security.controller;


import com.demo.launchpad.security.JwtManager;
import com.demo.launchpad.security.dto.AuthenticationRequest;
import com.demo.launchpad.security.dto.AuthenticationResponse;
import com.demo.launchpad.security.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtManager jwtManager;

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest loginRequest) {
        try {
            Authentication authentication = authenticationService.authenticate(
                    loginRequest.username(),
                    loginRequest.password()
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtManager.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
}
