package com.yettensyvus.sms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "studenti")
@Data
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String nume;
    private int varsta;
    private String email;
    private String specialitate;
    private int anStudiu;
    private double medie;
    private boolean bursier;

    @Column(name = "data_inscrierii")
    private LocalDate dataInscrierii;

    @ManyToMany(mappedBy = "studenti")
    @JsonBackReference  // Prevent infinite recursion
    private List<Curs> cursuri;

    public Student(String nume, int varsta, String email, String specialitate,
                   int anStudiu, double medie, boolean bursier, LocalDate dataInscrierii, List<Curs> cursuri) {
        this.nume = nume;
        this.varsta = varsta;
        this.email = email;
        this.specialitate = specialitate;
        this.anStudiu = anStudiu;
        this.medie = medie;
        this.bursier = bursier;
        this.dataInscrierii = dataInscrierii;
        this.cursuri = cursuri;
    }
}
