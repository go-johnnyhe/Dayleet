package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepo extends JpaRepository<user, Long> {
    user findByUsername(String username);
}
