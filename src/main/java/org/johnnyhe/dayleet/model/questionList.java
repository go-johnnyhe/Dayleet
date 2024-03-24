package org.johnnyhe.dayleet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class questionList {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "question_collection_id", referencedColumnName = "id")
    private questionCollection questionCollection;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private question question;

}
