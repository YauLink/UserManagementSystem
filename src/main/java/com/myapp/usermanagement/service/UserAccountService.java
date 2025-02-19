package com.myapp.usermanagement.service;

import com.myapp.usermanagement.dto.UserAccountDTO;
import com.myapp.usermanagement.model.UserAccount;
import com.myapp.usermanagement.repository.UserAccountRepository;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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
        List<UserAccount> userList = userAccountRepository.findByUsername(username);
        if (!userList.isEmpty()) {
            UserAccount user = userList.get(0); //as of now take first found user with this username
            return user.getPassword().equals(rawPassword);
        }
        return false;  // Username not found
    }
}
