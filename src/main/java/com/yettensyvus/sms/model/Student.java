package com.yettensyvus.sms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "studenti")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_nume", nullable = false)
    private String nume;

    @Column(name = "student_varsta", nullable = false)
    private int varsta;

    @Column(name = "student_email", nullable = false)
    private String email;

    @Column(name = "student_specialitate", nullable = false)
    private String specialitate;

    @Column(name = "student_an_studiu", nullable = false)
    private int anStudiu;

    @Column(name = "student_medie", nullable = false)
    private double medie;

    @Column(name = "student_bursier", nullable = false)
    private boolean bursier;

    @Column(name = "data_inscrierii", nullable = false)
    private LocalDate dataInscrierii;

    @ManyToMany(mappedBy = "studenti")
    private List<Curs> cursuri = new ArrayList<>();

}