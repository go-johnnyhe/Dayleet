package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.questionTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface questionTestCaseRepo extends JpaRepository<questionTestCase, Long> {
    List<questionTestCase> findByQuestionId(Long questionId);
}
