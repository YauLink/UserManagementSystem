package com.myapp.usermanagement.repository;

import com.myapp.usermanagement.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<UserAccount> findById(Long id);

    List<UserAccount> findAll();

    @Query("SELECT u FROM UserAccount u WHERE u.status = :status AND u.deletionRequestedAt < :date")
    List<UserAccount> findUsersForDeletion(
            @Param("status") UserAccount.Status status,
            @Param("date") LocalDateTime date);
}
