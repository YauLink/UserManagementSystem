package com.myapp.usermanagement.service;

import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserAccountRepository repository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void migratesLegacyPlaintextPasswordAfterSuccessfulLogin() {
        UserAccount user = activeUser("legacy-password");

        when(repository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("legacy-password")).thenReturn("$2b$encoded");
        when(jwtService.generateToken(7L, "john", "John Doe", "USER"))
                .thenReturn("jwt");

        var response = authService.login("john", "legacy-password");

        assertEquals("jwt", response.getToken());
        assertEquals("$2b$encoded", user.getPassword());
        verify(repository).save(user);
    }

    @Test
    void rejectsInvalidPasswordWithoutIssuingToken() {
        UserAccount user = activeUser("stored-password");

        when(repository.findByUsername("john")).thenReturn(Optional.of(user));

        var response = authService.login("john", "wrong");

        assertNull(response.getToken());
        verifyNoInteractions(jwtService);
        verify(repository, never()).save(any());
    }

    private UserAccount activeUser(String password) {
        UserAccount user = new UserAccount();
        user.setId(7L);
        user.setUsername("john");
        user.setPassword(password);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(UserAccount.Role.USER);
        user.setStatus(UserAccount.Status.ACTIVE);
        return user;
    }
}
