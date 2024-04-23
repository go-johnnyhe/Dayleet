package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.Difficulty;
import org.johnnyhe.dayleet.model.question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface questionRepo extends JpaRepository<question, Long> {
    Optional<question> findById(Long questionId);
    Optional<question> findByName(String name);
    boolean existsByNameAndCategoryAndDifficultyAndDescription(String name, String category, Difficulty difficulty, String description);
    Optional<question> findByNameAndCategoryAndDifficultyAndDescription(String name, String category, Difficulty difficulty, String description);
}