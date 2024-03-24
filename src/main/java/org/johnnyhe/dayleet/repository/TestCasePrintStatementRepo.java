package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.TestCasePrintStatement;
import org.johnnyhe.dayleet.model.codingLang;
import org.johnnyhe.dayleet.model.question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCasePrintStatementRepo extends JpaRepository<TestCasePrintStatement, Long> {
//    List<TestCasePrintStatement> findByQuestionIdAndLanguageId(int questionId, int languageId);
List<TestCasePrintStatement> findByQuestionAndLanguage(question question, codingLang language);

    TestCasePrintStatement findByQuestionAndLanguageAndId(question question, codingLang language, Long id);

}
