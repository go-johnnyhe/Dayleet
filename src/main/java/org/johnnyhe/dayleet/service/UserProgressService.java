package org.johnnyhe.dayleet.service;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.johnnyhe.dayleet.model.ReviewStatus;
import org.johnnyhe.dayleet.model.question;
import org.johnnyhe.dayleet.model.user;
import org.johnnyhe.dayleet.model.userProgress;
import org.johnnyhe.dayleet.repository.questionRepo;
import org.johnnyhe.dayleet.repository.userProgressRepo;
import org.johnnyhe.dayleet.repository.userRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserProgressService {
    private static final Logger log = LoggerFactory.getLogger(UserProgressService.class);

    @Autowired
    private userProgressRepo myUserProgressRepo;
    @Autowired
    private userRepo myUserRepo;
    @Autowired
    private questionRepo myQuestionRepo;


    // create a submit rating function --> this will
    // 1. get the unique question for the user from "userProgress"
    // 2. turn status to complete
    // 3. calculate the next interval for review
    public void submitRating(Long userId, Long questionId, int rating) {
        Optional<user> currUserOpt = myUserRepo.findById(userId);
        Optional<question> currQuestionOpt = myQuestionRepo.findById(questionId);

        if (!currUserOpt.isPresent() || !currQuestionOpt.isPresent()) {
            log.warn("Attempt to rate non-existing user or question: userId={}, questionId={}", userId, questionId);
            return;
        }

        user currUser = currUserOpt.get();
        question currQuestion = currQuestionOpt.get();
        userProgress myUserProgress = myUserProgressRepo.findByUserIdAndQuestionId((int) userId.longValue(), (int) questionId.longValue());

        if (myUserProgress == null) {
            log.info("Creating new userProgress for first-time interaction: userId={}, questionId={}", userId, questionId);
            myUserProgress = new userProgress();
            myUserProgress.setUser(currUser);
            myUserProgress.setQuestion(currQuestion);
            myUserProgress.setLastReviewDate(LocalDate.now());
            myUserProgress.setRepetition(0);
            myUserProgress.setEaseFactor(2.5); // Default starting ease factor
            myUserProgress.setReviewInterval(1); // Default interval
        }

        applySm2Algorithm(myUserProgress, rating);
        myUserProgressRepo.save(myUserProgress);
        log.debug("Updated userProgress: {}", myUserProgress);
    }


    // get all review questions for the day for the user
    public List<question> getReviewQuestions(int userId) {
        LocalDate currDate = LocalDate.now();
        List<userProgress> userProgressQuestions = myUserProgressRepo.findByUserIdAndNextReviewDateLessThanEqual(userId, currDate);

        List<question> questionsInProgress = userProgressQuestions.stream()
                .map(userProgress::getQuestion)
                .collect(Collectors.toList());

        return questionsInProgress;
    }



    // get all new untouched questions
    // check interval for review questions
    // show question in database is untouched
//    public List<question> getNewQuestions(int userId) {
//        List<question> allQuestions = myQuestionRepo.findAll();
//        List<userProgress> userProgresses = myUserProgressRepo.findByUserId(userId);
//
//        Set<Integer> touchedQuestionIds = userProgresses.stream()
//                .map(up -> up.getQuestion().getId())
//                .collect(Collectors.toSet());
//
//        return allQuestions.stream()
//                .filter(q -> !touchedQuestionIds.contains(q.getId()))
//                .collect(Collectors.toList());
//    }

//    helper here
private void applySm2Algorithm(userProgress up, int score) {
    double newEfactor = Math.max(1.3, up.getEaseFactor() + (0.1 - (5 - score) * (0.08 + (5 - score) * 0.02)));

    if (score < 3) {
        up.setRepetition(0);
        up.setReviewInterval(1);
    } else {
        int newRepetition = up.getRepetition() + 1;
        up.setRepetition(newRepetition);
        if (newRepetition == 1) {
            up.setReviewInterval(1);
        } else if (newRepetition == 2) {
            up.setReviewInterval(6);
        } else {
            up.setReviewInterval((int) Math.ceil(up.getReviewInterval() * newEfactor));
        }
    }
    up.setEaseFactor(newEfactor);
    up.setLastReviewDate(LocalDate.now());
    up.setNextReviewDate(LocalDate.now().plusDays((long) up.getReviewInterval()));
    up.setReviewStatus(ReviewStatus.Completed);
}
}
