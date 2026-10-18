package com.myapp.usermanagement.service;

import com.myapp.usermanagement.dto.AuthResponseDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.repository.UserAccountRepository;
import com.myapp.usermanagement.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserAccountRepository userAccountRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {

        this.userAccountRepository = userAccountRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO login(String username, String password) {

        Optional<UserAccount> userOptional =
                userAccountRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return new AuthResponseDTO(
                    null,
                    null,
                    "Invalid username or password",
                    null
            );
        }

        UserAccount user = userOptional.get();

        if (user.getStatus() != UserAccount.Status.ACTIVE) {
            return new AuthResponseDTO(
                    null,
                    null,
                    "User account is inactive",
                    null
            );
        }

        if (!matchesPassword(password, user.getPassword())) {
            return new AuthResponseDTO(
                    null,
                    null,
                    "Invalid username or password",
                    null
            );
        }

        // Migrate legacy plaintext records after a successful login.
        if (!isEncoded(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password));
            userAccountRepository.save(user);
        }

        String fullName =
                user.getFirstName() + " " + user.getLastName();

        String token =
                jwtService.generateToken(
                        user.getId(),
                        user.getUsername(),
                        fullName,
                        user.getRole().name()
                );

        return new AuthResponseDTO(
                user.getId(),
                fullName,
                "Authentication successful",
                token
        );
    }

    private boolean matchesPassword(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }

        if (isEncoded(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        return MessageDigest.isEqual(
                rawPassword.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8)
        );
    }

    private boolean isEncoded(String password) {
        return password != null && password.startsWith("$2");
    }
}
