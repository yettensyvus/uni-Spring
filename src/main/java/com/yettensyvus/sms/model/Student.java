package com.yettensyvus.sms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
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

    @ManyToMany(
            mappedBy = "studenti",
            fetch = FetchType.LAZY
    )
    @JsonBackReference
    private List<Curs> cursuri;
}