package com.yettensyvus.sms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursuri")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Curs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "curs_denumire", nullable = false)
    private String denumire;

    @Column(name = "curs_credite", nullable = false)
    private int credite;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    @ManyToMany(mappedBy = "cursuri", fetch = FetchType.LAZY)
    private List<Student> studenti = new ArrayList<>();

}