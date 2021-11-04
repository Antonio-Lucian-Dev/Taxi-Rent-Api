package com.asusoftware.taxirentapi.registration.service;

import com.asusoftware.taxirentapi.registration.model.RegistrationRequest;
import com.asusoftware.taxirentapi.user.model.User;
import com.asusoftware.taxirentapi.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;

    public String register(RegistrationRequest registrationRequest) {
        boolean isEmailValid = emailValidator.test(registrationRequest.getEmail());
        if(!isEmailValid) {
            throw new IllegalStateException("Email not valid");
        }
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        user.setUserRole(registrationRequest.getUserRole());
        // TODO: add birthday
        return userService.signUpUser(
               user
        );
    }
}
