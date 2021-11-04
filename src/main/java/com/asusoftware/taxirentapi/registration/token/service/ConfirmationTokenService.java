package com.asusoftware.taxirentapi.registration.token.service;

import com.asusoftware.taxirentapi.registration.token.model.ConfirmationToken;
import com.asusoftware.taxirentapi.registration.token.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }
}
