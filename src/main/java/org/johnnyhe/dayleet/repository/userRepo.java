package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepo extends JpaRepository<user, Long> {
    Optional<user> findById(Long userId);
    Optional<user> findByUsername(String username);
}
