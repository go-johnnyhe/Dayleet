package org.johnnyhe.dayleet.service;

import org.johnnyhe.dayleet.model.question;
import org.johnnyhe.dayleet.model.questionTestCase;
import org.johnnyhe.dayleet.repository.questionRepo;
import org.johnnyhe.dayleet.repository.questionTestCaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {
    private final questionRepo myQuestionRepo;
    private final questionTestCaseRepo myQuestionTestCaseRepo;

    @Autowired
    public ProblemService(questionRepo myQuestionRepo, questionTestCaseRepo myQuestionTestCaseRepo) {
        this.myQuestionRepo = myQuestionRepo;
        this.myQuestionTestCaseRepo = myQuestionTestCaseRepo;
    }

    public question findQuestionById(Long questionId) {
        return myQuestionRepo.findById(questionId).orElseThrow(() -> new IllegalArgumentException("Question not found with ID: " + questionId));
    }

    public List<String> getTestCases(Long questionId) {
        List<questionTestCase> testCases = myQuestionTestCaseRepo.findByQuestionId(questionId);
        return testCases.stream().map(questionTestCase::getTestCase).collect(Collectors.toList());
    }

    public List<String> getExpectedOutputs(Long questionId) {
        List<questionTestCase> testCases = myQuestionTestCaseRepo.findByQuestionId(questionId);
        return testCases.stream().map(questionTestCase::getCorrectOutput).collect(Collectors.toList());
    }
}
