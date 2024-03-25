package org.johnnyhe.dayleet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "test_case_print_statement")
public class TestCasePrintStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private codingLang language;


    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private question question;

    @Column(name = "print_statement")
    private String printStatement;

    @Override
    public String toString() {
        return "TestCasePrintStatement{" +
                "id=" + id +
                ", language=" + language +
                ", question=" + question.getName() +
                ", printStatement='" + printStatement + '\'' +
                '}';
    }

}

