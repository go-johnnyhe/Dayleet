package org.johnnyhe.dayleet.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class user {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private boolean completedSetup;
    private String questionSet;
    private int numDailyProblems;
    private LocalDate completionDate;
    private boolean shuffledView;
    private boolean darkMode;
    private String password;

}
