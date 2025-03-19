package com.myapp.usermanagement.controller;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.service.UserAccountService;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private final UserAccountService userAccountService;

    @Autowired
    public AdminController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @RequestMapping("/users")
    public String showAllUsers(Model model) {
        List<UserAccount> users = new ArrayList<>(userAccountService.fetchAllUsers());

        Map<String, List<UserAccount>> map = new HashMap<>();
        map.put("users", users);
        model.addAllAttributes(map);
        return "userList";
    }

    // Show user details by username
    @GetMapping("/users/username/{username}")
    public String showUserByUsername(@PathVariable String username, Model model) {
        UserAccount user = userAccountService.fetchByUsername(username);
        if (user == null) {
            return "redirect:/admin/users?error=UserNotFound";
        }
        model.addAttribute("user", user);
        return "userView";
    }

    // Show the user creation form
    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new UserAccountDTO());
        return "userEdit";
    }

    // Handle user creation (form submission)
    @PostMapping("/users/create")
    public String createUser(@ModelAttribute("user") UserAccountDTO accountDTO) {
        userAccountService.createUser(accountDTO);
        return "redirect:/admin/users?success=UserCreated";
    }

    // Show edit form for an existing user
    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        UserAccount user = userAccountService.fetchById(id);
        if (user == null) {
            return "redirect:/admin/users?error=UserNotFound";
        }
        model.addAttribute("user", user);
        return "userEdit";
    }

    // Handle user update (form submission)
    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") UserAccountDTO accountDTO) {
        userAccountService.updateUserInformation(id, accountDTO);
        return "redirect:/admin/users?success=UserUpdated";
    }

    // Soft delete user
    @GetMapping("/users/delete/{id}")
    public String softDeleteUser(@PathVariable Long id) {
        boolean deleted = userAccountService.softDeleteUser(id);
        return deleted ? "redirect:/admin/users?success=UserDeactivated"
                : "redirect:/admin/users?error=UserNotFound";
    }

    // Hard delete user
    @GetMapping("/users/hard-delete/{id}")
    public String hardDeleteUser(@PathVariable Long id) {
        boolean deleted = userAccountService.hardDeleteUser(id);
        return deleted ? "redirect:/admin/users?success=UserDeleted"
                : "redirect:/admin/users?error=UserNotFound";
    }

    // Cleanup inactive users
    @GetMapping("/cleanup")
    public String cleanupInactiveUsers(@RequestParam(defaultValue = "30") int days) {
        int deletedCount = userAccountService.permanentlyDeleteInactiveUsers(days);
        return "redirect:/admin/users?success=" + deletedCount + " users permanently deleted";
    }
}
