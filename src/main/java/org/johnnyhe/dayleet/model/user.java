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
    private String name;
    private LocalDate firstTimeLogin;
    private int numDailyProblems;
    private boolean shuffledView;
    private boolean darkMode;
    private String password;
    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstTimeLogin=" + firstTimeLogin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        user user = (user) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(firstTimeLogin, user.firstTimeLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, firstTimeLogin);
    }
}
