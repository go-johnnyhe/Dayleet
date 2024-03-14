package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.questionList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface questionListRepo extends JpaRepository<questionList, Long> {
}
