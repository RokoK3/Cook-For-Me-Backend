package com.teamprogress.backend.persistance.repository;

import com.teamprogress.backend.persistance.entity.User;
import com.teamprogress.backend.persistance.entity.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndStatus(String username, AccountStatus status);

}
