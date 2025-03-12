package com.yettensyvus.sms.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "profesori")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") // Add this
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nume;
    private String materie;
    private int experientaAni;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curs> cursuri; // Remove @JsonBackReference

    public Profesor(String nume, String materie, int experientaAni, List<Curs> cursuri) {
        this.nume = nume;
        this.materie = materie;
        this.experientaAni = experientaAni;
        this.cursuri = cursuri;
    }
}