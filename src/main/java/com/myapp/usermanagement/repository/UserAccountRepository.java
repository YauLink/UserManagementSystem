package com.myapp.usermanagement.repository;

import com.myapp.usermanagement.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    List<UserAccount> findByUsername(String username);
    List<UserAccount> findByRole(UserAccount.Role role);
    List<UserAccount> findByStatus(UserAccount.Status status);
}
