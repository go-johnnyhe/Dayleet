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
@AllArgsConstructor
@NoArgsConstructor
public class questionPlaceHolder {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String placeHolder;

    @ManyToOne
    @JoinColumn(name="coding_lang_id", referencedColumnName = "id")
    private codingLang codingLanguage;

    @ManyToOne
    @JoinColumn(name="question_id", referencedColumnName = "id")
    private question question;

    @Override
    public String toString() {
        return "questionPlaceHolder{" +
                "id=" + id +
                ", placeHolder='" + placeHolder + '\'' +
                ", codingLanguage=" + codingLanguage +
                ", question=" + question +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        questionPlaceHolder that = (questionPlaceHolder) o;
        return id == that.id && Objects.equals(placeHolder, that.placeHolder) && codingLanguage == that.codingLanguage && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placeHolder, codingLanguage, question);
    }
}
