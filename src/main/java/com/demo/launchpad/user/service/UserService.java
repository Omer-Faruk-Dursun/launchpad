package com.demo.launchpad.user.service;

import com.demo.launchpad.security.ErrorCode;
import com.demo.launchpad.security.ServiceException;
import com.demo.launchpad.user.dto.UserRegistrationRequest;
import com.demo.launchpad.user.entity.Role;
import com.demo.launchpad.user.entity.User;
import com.demo.launchpad.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserRegistrationRequest registrationRequest) {
        if (userRepository.findByUsername(registrationRequest.username()).isPresent()) {
            throw new ServiceException(ErrorCode.USER_ALREADY_EXISTS.getMessage(), ErrorCode.USER_ALREADY_EXISTS.getCode().toString());
        }


        User user = new User();
        user.setUsername(registrationRequest.username());
        user.setFirstname(registrationRequest.firstname());
        user.setLastname(registrationRequest.lastname());
        user.setEmail(registrationRequest.email());
        user.setPassword(passwordEncoder.encode(registrationRequest.password()));
        user.setRoles(registrationRequest.roles().stream()
                .map(role -> new Role(null, role))
                .toList());

        userRepository.save(user);
    }

}
