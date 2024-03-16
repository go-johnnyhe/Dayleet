package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.codingLang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface codingLangRepo extends JpaRepository<codingLang, Long> {
    Optional<codingLang> findByName(String name);
}
