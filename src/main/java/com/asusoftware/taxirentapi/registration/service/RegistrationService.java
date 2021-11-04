package com.asusoftware.taxirentapi.registration.service;

import com.asusoftware.taxirentapi.registration.model.RegistrationRequest;
import com.asusoftware.taxirentapi.registration.token.model.ConfirmationToken;
import com.asusoftware.taxirentapi.registration.token.service.ConfirmationTokenService;
import com.asusoftware.taxirentapi.user.model.User;
import com.asusoftware.taxirentapi.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

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

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }
}
