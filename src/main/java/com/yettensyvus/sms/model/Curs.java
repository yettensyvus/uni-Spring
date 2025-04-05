package com.yettensyvus.sms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cursuri")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denumire;
    private int credite;

    @ManyToOne
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @ManyToMany
    @JoinTable(
            name = "student_curs",
            joinColumns = @JoinColumn(name = "curs_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> studenti;

}