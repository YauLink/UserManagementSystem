package com.myapp.usermanagement.service;

import com.myapp.usermanagement.dto.AuthResponseDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserAccountRepository userAccountRepository,
                       PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO login(String username, String password) {

        Optional<UserAccount> userOptional = userAccountRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return new AuthResponseDTO(
                    null,
                    null,
                    "Invalid username or password"
            );
        }

        UserAccount user = userOptional.get();

        if (user.getStatus() != UserAccount.Status.ACTIVE) {
            return new AuthResponseDTO(
                    null,
                    null,
                    "User account is inactive"
            );
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new AuthResponseDTO(
                    null,
                    null,
                    "Invalid username or password"
            );
        }

        String fullName = user.getFirstName() + " " + user.getLastName();

        return new AuthResponseDTO(
                user.getId(),
                fullName,
                "Authentication successful"
        );
    }
}
