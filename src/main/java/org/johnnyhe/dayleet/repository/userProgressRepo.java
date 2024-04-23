package org.johnnyhe.dayleet.repository;

import org.johnnyhe.dayleet.model.question;
import org.johnnyhe.dayleet.model.userProgress;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface userProgressRepo extends JpaRepository<userProgress, Long> {
    List<userProgress> findByUserIdAndNextReviewDateLessThanEqual(int userId, LocalDate currentDate);
    userProgress findByUserIdAndQuestionId(int userId, int questionId);

    List<userProgress> findByUserId(int userId);
}
