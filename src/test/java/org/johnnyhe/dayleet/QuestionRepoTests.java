package org.johnnyhe.dayleet;

import org.johnnyhe.dayleet.model.Difficulty;
import org.johnnyhe.dayleet.model.question;
import org.johnnyhe.dayleet.model.questionTestCase;
import org.johnnyhe.dayleet.repository.questionPlaceholderRepo;
import org.johnnyhe.dayleet.repository.questionRepo;
import org.johnnyhe.dayleet.repository.questionTestCaseRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepoTests {
    @Autowired
    private questionRepo myQuestionRepo;
    @Autowired
    private questionTestCaseRepo myQuestionTestCaseRepo;
    @Autowired
    private questionPlaceholderRepo myQuestionPlaceholderRepo;

    @Test
    void findQuestionById_shouldReturnTestCases() {
        question myQuestion = new question();
        long myId = myQuestion.getId();
        myQuestionRepo.save(myQuestion);

        myQuestionRepo.flush();

        questionTestCase myQuestionTestCase1 = new questionTestCase();
        myQuestionTestCase1.setQuestion(myQuestion);
        myQuestionTestCaseRepo.save(myQuestionTestCase1);

        questionTestCase myQuestionTestCase2 = new questionTestCase();
        myQuestionTestCase2.setQuestion(myQuestion);
        myQuestionTestCaseRepo.save(myQuestionTestCase2);

        List<questionTestCase> testCases = myQuestionTestCaseRepo.findByQuestionId(myId);
        System.out.println(testCases);
        assertThat(testCases).isNotNull();
    }

//    @Test
//    void findByName_shouldReturnQuestion() {
//        String questionName = "Shortest Way Home";
//        question myQuestion = new question();
//        myQuestion.setName(questionName);
//        myQuestionRepo.save(myQuestion);
//
//        question questionFound = myQuestionRepo.findByName(questionName);
//        assertThat(questionFound.getName()).isEqualTo(questionName);
//
//    }

    @Test
    void existsByNameAndCategoryAndDifficultyAndDescription_shouldReturnTrue() {
        String name = "my test question";
        String category = "made-up category";
        Difficulty myDifficulty = Difficulty.Hard;
        String description = "my test description";

        question myQuestion = new question(name, category, myDifficulty, description);
        myQuestionRepo.save(myQuestion);

        boolean exists = myQuestionRepo.existsByNameAndCategoryAndDifficultyAndDescription(name, category, myDifficulty, description);
        assertThat(exists).isEqualTo(true);
    }

}
