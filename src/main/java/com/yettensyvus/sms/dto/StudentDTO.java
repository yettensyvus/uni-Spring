// StudentDTO.java
package com.yettensyvus.sms.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class StudentDTO {
    private Long id;
    private String nume;
    private int varsta;
    private String email;
    private String specialitate;
    private int anStudiu;
    private double medie;
    private boolean bursier;
    private LocalDate dataInscrierii;
    private List<Long> cursuriIds = new ArrayList<>();
}