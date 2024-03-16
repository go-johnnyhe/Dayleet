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

    @ManyToOne
    @JoinColumn(name="question_id", referencedColumnName = "id")
    private question question;

}
