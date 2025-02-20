package com.myapp.usermanagement.service;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.repository.UserAccountRepository;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Transactional
    public UserAccount createUser(UserAccountDTO accountDTO) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(accountDTO.getUsername());
        userAccount.setPassword(accountDTO.getPassword());
        userAccount.setFirstName(accountDTO.getFirstName());
        userAccount.setLastName(accountDTO.getLastName());
        userAccount.setRole(accountDTO.getRole());
        userAccount.setStatus(accountDTO.getStatus());
        userAccount.setCreated(accountDTO.getCreated());

        return userAccountRepository.save(userAccount);
    }

    @Transactional
    public boolean validateLogin(String username, String rawPassword) {
        Optional<UserAccount> user = userAccountRepository.findByUsername(username);
        if (user.isPresent()) {
            UserAccount userAccount = user.get(); //as of now take first found user with this username
            return userAccount.getPassword().equals(rawPassword);
        }
        return false;  // Username not found
    }

    public UserAccount updateStatus(Long userId, UserAccount.Status status) {
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        if(user.isPresent()) {
            UserAccount userAccount = user.get();
            userAccount.setStatus(status);
            return userAccountRepository.save(userAccount);
        }
        return null;
    }

    public UserAccount updateRole(Long userId, UserAccount.Role role) {
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        if(user.isPresent()) {
            UserAccount userAccount = user.get();
            userAccount.setRole(role);
            return userAccountRepository.save(userAccount);
        }
        return null;
    }

    public UserAccount updateUserInformation(Long userId, UserAccountDTO accountDTO) {
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        if(user.isPresent()) {
            UserAccount userAccount = user.get();
            userAccount.updateInfo(
                    accountDTO.getLastName(),
                    accountDTO.getFirstName(),
                    accountDTO.getPassword(),
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
}
