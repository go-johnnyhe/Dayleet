package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.questionList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface questionListRepo extends JpaRepository<questionList, Long> {
    List<questionList> findByQuestionCollectionName(String questionCollectionName);
}
