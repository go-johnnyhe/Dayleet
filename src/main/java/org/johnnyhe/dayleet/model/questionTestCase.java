package org.johnnyhe.dayleet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class questionTestCase {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String testCase;
    private String correctOutput;
    private String checkCorrectnessCode;

    @ManyToOne
    @JoinColumn(name = "coding_lang_id", referencedColumnName = "id")
    private codingLang codingLanguage;

    @ManyToOne
    @JoinColumn(name="question_id", referencedColumnName = "id")
    private question question;

    @Override
    public String toString() {
        return "questionTestCase{" +
                "id=" + id +
                ", testCase='" + testCase + '\'' +
                ", correctOutput='" + correctOutput + '\'' +
                ", checkCorrectnessCode='" + checkCorrectnessCode + '\'' +
                ", codingLanguage=" + codingLanguage +
                ", question=" + question +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        questionTestCase that = (questionTestCase) o;
        return id == that.id && Objects.equals(testCase, that.testCase) && Objects.equals(correctOutput, that.correctOutput) && Objects.equals(checkCorrectnessCode, that.checkCorrectnessCode) && codingLanguage == that.codingLanguage && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testCase, correctOutput, checkCorrectnessCode, codingLanguage, question);
    }
}
