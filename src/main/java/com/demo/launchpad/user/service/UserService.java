package com.demo.launchpad.user.service;

import com.demo.launchpad.user.dto.UserRegistrationRequest;
import com.demo.launchpad.user.entity.Role;
import com.demo.launchpad.user.entity.User;
import com.demo.launchpad.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserRegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.username());
        user.setEmail(registrationRequest.email());
        user.setPassword(passwordEncoder.encode(registrationRequest.password()));
        user.setRoles(registrationRequest.roles().stream().map(role -> new Role(null, role)).toList());
        userRepository.save(user);
        log.info("User registered successfully.");
    }

}
