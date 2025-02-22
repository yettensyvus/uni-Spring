package com.yettensyvus.sms;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String nume;
    private short varsta;
    private String email;
    private String specialitate;
    private int anStudiu;
    private double medie;
    private boolean bursier;
    private LocalDate dataInscrierii;
}
