package com.yettensyvus.sms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profesori")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profesor_nume", nullable = false)
    private String nume;

    @Column(name = "profesor_materie", nullable = false)
    private String materie;

    @Column(name = "profesor_experienta_ani", nullable = false)
    private int experientaAni;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Curs> cursuri = new ArrayList<>();

}