package com.myapp.usermanagement.service;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private UserAccountRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAccountService service;

    @Test
    void createUserStoresOnlyAnEncodedPassword() {
        UserAccountDTO dto = new UserAccountDTO();
        dto.setUsername("john");
        dto.setPassword("plain-secret");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setRole(UserAccount.Role.USER);
        dto.setStatus(UserAccount.Status.ACTIVE);
        when(passwordEncoder.encode("plain-secret")).thenReturn("encoded-secret");
        when(repository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAccount saved = service.createUser(dto);

        assertEquals("encoded-secret", saved.getPassword());
        assertNotEquals("plain-secret", saved.getPassword());
        verify(passwordEncoder).encode("plain-secret");
    }

    @Test
    void inactiveUsersCannotAuthenticateThroughUserDetailsService() {
        UserAccount user = user(UserAccount.Status.INACTIVE);
        when(repository.findByUsername("john")).thenReturn(Optional.of(user));

        assertThrows(DisabledException.class, () -> service.loadUserByUsername("john"));
    }

    @Test
    void negativeCleanupWindowIsRejectedWithoutDeletingUsers() {
        assertThrows(IllegalArgumentException.class,
                () -> service.permanentlyDeleteInactiveUsers(-1));

        verifyNoInteractions(repository);
    }

    private UserAccount user(UserAccount.Status status) {
        UserAccount user = new UserAccount();
        user.setUsername("john");
        user.setPassword("encoded-secret");
        user.setRole(UserAccount.Role.USER);
        user.setStatus(status);
        return user;
    }
}
