package com.yettensyvus.sms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cursuri")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // Add this
public class Curs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denumire;
    private int credite;

    @ManyToOne
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor; // Remove @JsonManagedReference

    @ManyToMany
    @JoinTable(
            name = "student_curs",
            joinColumns = @JoinColumn(name = "curs_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore // Keep this to avoid circular reference with Student
    private List<Student> studenti;

    public Curs(String denumire, int credite, Profesor profesor, List<Student> studenti) {
        this.denumire = denumire;
        this.credite = credite;
        this.profesor = profesor;
        this.studenti = studenti;
    }
}