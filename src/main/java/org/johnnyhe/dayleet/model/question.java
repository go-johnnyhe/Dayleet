package org.johnnyhe.dayleet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category", "difficulty", "description"}))
public class question {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String category;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @Column(length = 1000)
    private String description;
    @OneToMany(mappedBy = "question")
    private List<questionTestCase> questionTestCases;

    public question(String name, String category, Difficulty difficulty, String description) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.description = description;
    }
}
