package com.yettensyvus.sms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cursuri")
@Data
@NoArgsConstructor
public class Curs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denumire;
    private int credite;

    @ManyToOne
    @JoinColumn(name = "profesor_id", nullable = false)
    @JsonManagedReference  // Prevent infinite recursion
    private Profesor profesor;

    @ManyToMany
    @JoinTable(
            name = "student_curs",
            joinColumns = @JoinColumn(name = "curs_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonManagedReference  // Prevent infinite recursion
    private List<Student> studenti;

    public Curs(String denumire, int credite, Profesor profesor, List<Student> studenti) {
        this.denumire = denumire;
        this.credite = credite;
        this.profesor = profesor;
        this.studenti = studenti;
    }
}
