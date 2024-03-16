package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.Difficulty;
import org.johnnyhe.dayleet.model.question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface questionRepo extends JpaRepository<question, Long> {
    question findByName(String name);
    boolean existsByNameAndCategoryAndDifficultyAndDescription(String name, String category, Difficulty difficulty, String description);
    question findByNameAndCategoryAndDifficultyAndDescription(String name, String category, Difficulty difficulty, String description);
}
