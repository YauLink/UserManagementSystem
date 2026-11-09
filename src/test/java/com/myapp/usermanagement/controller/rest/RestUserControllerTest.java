package com.myapp.usermanagement.controller.rest;

import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestUserControllerTest {

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RestUserController controller;

    @Test
    void getUserByIdDoesNotAllowInsecureDirectObjectReference() {
        UserAccount target = user("victim");
        when(userAccountService.fetchById(2L)).thenReturn(target);
        when(authentication.getName()).thenReturn("attacker");

        ResponseEntity<?> response = controller.getUserById(2L, authentication);

        assertEquals(403, response.getStatusCodeValue());
        verify(userAccountService).fetchById(2L);
        verifyNoMoreInteractions(userAccountService);
    }

    @Test
    void updateUserDoesNotModifyAnotherUsersAccount() {
        UserAccount target = user("victim");
        when(userAccountService.fetchById(2L)).thenReturn(target);
        when(authentication.getName()).thenReturn("attacker");

        ResponseEntity<?> response = controller.updateUser(2L, null, authentication);

        assertEquals(403, response.getStatusCodeValue());
        verify(userAccountService, never()).updateUserInformation(anyLong(), any());
    }

    @Test
    void deletionAndRecoveryAreRestrictedToTheAuthenticatedUser() {
        UserAccount target = user("victim");
        when(userAccountService.fetchById(2L)).thenReturn(target);
        when(authentication.getName()).thenReturn("attacker");

        assertEquals(403, controller.requestAccountDeletion(2L, authentication).getStatusCodeValue());
        assertEquals(403, controller.recoverAccount(2L, authentication).getStatusCodeValue());

        verify(userAccountService, never()).requestAccountDeletion(anyLong());
        verify(userAccountService, never()).recoverAccount(anyLong());
    }

    private UserAccount user(String username) {
        UserAccount user = new UserAccount();
        user.setId(2L);
        user.setUsername(username);
        user.setStatus(UserAccount.Status.ACTIVE);
        user.setRole(UserAccount.Role.USER);
        return user;
    }
}
