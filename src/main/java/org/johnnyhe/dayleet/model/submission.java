package org.johnnyhe.dayleet.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class submission {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="question_id", referencedColumnName = "id")
    private question myQuestion;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private user myUser;

    private LocalDate submissionTime;

    private String submittedCode;

    private String submissionStatus;

    private String judge0Token;

}
