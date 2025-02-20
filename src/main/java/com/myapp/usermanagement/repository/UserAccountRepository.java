package com.myapp.usermanagement.repository;

import com.myapp.usermanagement.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);
    List<UserAccount> findByRole(UserAccount.Role role);
    List<UserAccount> findByStatus(UserAccount.Status status);
}
