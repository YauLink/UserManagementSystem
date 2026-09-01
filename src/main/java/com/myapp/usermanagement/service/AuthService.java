package com.myapp.usermanagement.service;

import com.myapp.usermanagement.dto.AuthResponseDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;

    public AuthService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public AuthResponseDTO login(String username, String password) {

        Optional<UserAccount> userOptional = userAccountRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return new AuthResponseDTO(
                    false,
                    null,
                    null,
                    null,
                    null,
                    "Invalid username or password"
            );
        }

        UserAccount user = userOptional.get();

        if (user.getStatus() != UserAccount.Status.ACTIVE) {
            return new AuthResponseDTO(
                    false,
                    null,
                    null,
                    null,
                    null,
                    "User account is inactive"
            );
        }

        if (!user.getPassword().equals(password)) {
            return new AuthResponseDTO(
                    false,
                    null,
                    null,
                    null,
                    null,
                    "Invalid username or password"
            );
        }

        String fullName = user.getFirstName() + " " + user.getLastName();

        return new AuthResponseDTO(
                true,
                "Authentication successful",
                user.getId(),
                fullName,
                user.getUsername(),
                user.getRole().name()

        );
    }
}