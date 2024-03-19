package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.codingLang;
import org.johnnyhe.dayleet.model.question;
import org.johnnyhe.dayleet.model.questionPlaceHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface questionPlaceholderRepo extends JpaRepository<questionPlaceHolder, Long> {
    questionPlaceHolder findByQuestionAndCodingLanguage(question problem, codingLang codingLanguage);
}
