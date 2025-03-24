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

    Optional<UserAccount> findById(Long id);

    List<UserAccount> findAll();

    @Query("SELECT u FROM UserAccount u WHERE u.createdDate < :date")
    List<UserAccount> findUsersForDeletion(@Param("date") LocalDateTime someDate);

    /*@Modifying
    @Query("DELETE FROM UserAccount u WHERE u.status = 'INACTIVE' AND u.deletionRequestedAt <= :threshold")
    int permanentlyDeleteInactiveUsers(@Param("threshold")LocalDateTime threshold);

    @Modifying
    @Query("UPDATE UserAccount u SET u.status = 'ACTIVE', u.deletionRequestedAt = NULL WHERE u.id = :userId")
    int recoverAccount(Long userId);

    @Modifying
    @Query("UPDATE UserAccount u SET u.status = 'INACTIVE', u.deletionRequestedAt = :deletionRequestedAt WHERE u.id = :userId")
    int requestAccountDeletion(Long userId, LocalDateTime deletionRequestedAt);*/

}
