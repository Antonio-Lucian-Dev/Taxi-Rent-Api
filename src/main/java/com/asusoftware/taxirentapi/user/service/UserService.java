package com.asusoftware.taxirentapi.user.service;

import com.asusoftware.taxirentapi.registration.token.model.ConfirmationToken;
import com.asusoftware.taxirentapi.registration.token.service.ConfirmationTokenService;
import com.asusoftware.taxirentapi.user.model.User;
import com.asusoftware.taxirentapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    // E usato per il login, come trovare gli users quando provano a fare login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public String signUpUser(User user) {
        boolean isUserExist = userRepository.findByEmail(user.getEmail())
                .isPresent();
        if(isUserExist) {
            throw new IllegalStateException("Email already taken");
        }
        // Convertiamo la password del user con bcrypt
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        // Salviamo la pssword convertita con bcrypt su quel specifico user
        user.setPassword(encodedPassword);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setConfirmedAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setUser(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        // TODO: Send email
        return token;
    }

    public int enableAppUser(String email) {
        return userRepository.enableUser(email);
    }
}
