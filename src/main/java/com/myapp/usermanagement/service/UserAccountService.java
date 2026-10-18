package com.myapp.usermanagement.service;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAccountService(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAccount createUser(UserAccountDTO accountDTO) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(accountDTO.getUsername());
        userAccount.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        userAccount.setFirstName(accountDTO.getFirstName());
        userAccount.setLastName(accountDTO.getLastName());
        userAccount.setRole(accountDTO.getRole());
        userAccount.setStatus(accountDTO.getStatus());
        userAccount.setCreatedDate(accountDTO.getCreated());

        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public boolean validateLogin(String username, String rawPassword) {
        Optional<UserAccount> user = userAccountRepository.findByUsername(username);
        if (user.isPresent()) {
            UserAccount userAccount = user.get(); //as of now take first found user with this username
            return passwordMatches(rawPassword, userAccount.getPassword());
        }
        return false;  // Username not found
    }

    @Transactional
    public boolean softDeleteUser(Long userId) {//make user inactive
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        if (user.isPresent()) {
            UserAccount userAccount = user.get();
            userAccount.setStatus(UserAccount.Status.INACTIVE);
            userAccountRepository.save(userAccount);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean hardDeleteUser(Long userId) { //permanently delete user
        if (userAccountRepository.existsById(userId)) {
            userAccountRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public UserAccount updateUserInformation(Long userId, UserAccountDTO accountDTO) {
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        if(user.isPresent()) {
            UserAccount userAccount = user.get();
            userAccount.updateInfo(
                    accountDTO.getLastName(),
                    accountDTO.getFirstName(),
                    passwordEncoder.encode(accountDTO.getPassword()),
                    accountDTO.getUsername()
            );
            return userAccountRepository.save(userAccount);
        }
        return null;
    }

    public UserAccount fetchById (Long userId) {
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        return user.orElse(null);
    }

    public UserAccount fetchByUsername (String userName) {
        Optional<UserAccount> user = userAccountRepository.findByUsername(userName);
        return user.orElse(null);
    }

    public List<UserAccount> fetchAllUsers() {
        return userAccountRepository.findAll();
    }

    @Transactional
    public boolean requestAccountDeletion(Long userId) {
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        if (user.isPresent()) {
            UserAccount userAccount = user.get();
            userAccount.setStatus(UserAccount.Status.INACTIVE);
            userAccount.setDeletionRequestedAt(LocalDateTime.now()); // Store deletion request time
            userAccountRepository.save(userAccount);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean recoverAccount(Long userId) {
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        if (user.isPresent() && user.get().getStatus() == UserAccount.Status.INACTIVE) {
            UserAccount userAccount = user.get();
            userAccount.setStatus(UserAccount.Status.ACTIVE);
            userAccount.setDeletionRequestedAt(null); // Clear deletion request timestamp
            userAccountRepository.save(userAccount);
            return true;
        }
        return false;
    }

    @Transactional
    public int permanentlyDeleteInactiveUsers(int days) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        List<UserAccount> usersToDelete = userAccountRepository.findUsersForDeletion(threshold);
        userAccountRepository.deleteAll(usersToDelete);
        return usersToDelete.size();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    private boolean passwordMatches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }

        if (storedPassword.startsWith("$2")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        return rawPassword.equals(storedPassword);
    }

}
