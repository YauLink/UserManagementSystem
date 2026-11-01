
package com.myapp.usermanagement.controller.web;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserAccountService userAccountService;

    public UserController(){
        userAccountService = null;
    };

    @Autowired
    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // Show exact user
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model, Authentication authentication) {
        UserAccount user = userAccountService.fetchById(id);
        if (user == null) {
            return "redirect:/users?error=UserNotFound";
        }
        if (!user.getUsername().equals(authentication.getName())) {
            return "redirect:/user?error=AccessDenied";
        }
        model.addAttribute("user", user);
        return "userView";
    }

    // Show user list
    @GetMapping
    public String showAllUsers(Model model) {
        List<UserAccount> users = userAccountService.fetchAllUsers();
        model.addAttribute("users", users);
        return "userList";
    }

    // Updating profile
    @PutMapping("/{id}/edit")
    public String updateUser(
            @PathVariable Long id,
            @RequestBody UserAccountDTO accountDTO,
            Authentication authentication) {
        UserAccount existing = userAccountService.fetchById(id);
        if (existing == null || !existing.getUsername().equals(authentication.getName())) {
            return "redirect:/user?error=AccessDenied";
        }
        userAccountService.updateUserInformation(id, accountDTO);
        return "redirect:/users/" + id;
    }

    // Account deletion
    @PostMapping("/{userId}/delete")
    public String requestAccountDeletion(@PathVariable Long userId, Authentication authentication) {
        UserAccount existing = userAccountService.fetchById(userId);
        if (existing == null || !existing.getUsername().equals(authentication.getName())) {
            return "redirect:/user?error=AccessDenied";
        }
        userAccountService.requestAccountDeletion(userId);
        return "redirect:/users";
    }

    // Account recovery
    @PostMapping("/{userId}/recover")
    public String recoverAccount(@PathVariable Long userId, Authentication authentication) {
        UserAccount existing = userAccountService.fetchById(userId);
        if (existing == null || !existing.getUsername().equals(authentication.getName())) {
            return "redirect:/user?error=AccessDenied";
        }
        userAccountService.recoverAccount(userId);
        return "redirect:/users";
    }
}
