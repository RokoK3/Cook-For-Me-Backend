package com.teamprogress.backend.persistance.repository;

import com.teamprogress.backend.persistance.entity.Cook;
import com.teamprogress.backend.persistance.entity.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CookRepository extends JpaRepository<Cook, Long> {

    boolean existsByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<Cook> findByUsername(String username);

    boolean existsByUsernameAndStatus(String username, AccountStatus status);

}
