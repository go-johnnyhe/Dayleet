package org.johnnyhe.dayleet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class userProgress {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate lastReviewDate;
    private LocalDate nextReviewDate;
    private double easeFactor;
    private double reviewInterval;
    private int repetition;
    private int score;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private user user;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private question question;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus = ReviewStatus.valueOf("Untouched");

    @Override
    public String toString() {
        return "userProgress{" +
                "id=" + id +
                ", lastReviewDate=" + lastReviewDate +
                ", nextReviewDate=" + nextReviewDate +
                ", easeFactor=" + easeFactor +
                ", reviewInterval=" + reviewInterval +
                ", repetition=" + repetition +
                ", score=" + score +
                ", user=" + user +
                ", question=" + question +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        userProgress that = (userProgress) o;
        return id == that.id && Double.compare(easeFactor, that.easeFactor) == 0 && reviewInterval == that.reviewInterval && repetition == that.repetition && score == that.score && Objects.equals(lastReviewDate, that.lastReviewDate) && Objects.equals(nextReviewDate, that.nextReviewDate) && Objects.equals(user, that.user) && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastReviewDate, nextReviewDate, easeFactor, reviewInterval, repetition, score, user, question);
    }
}
