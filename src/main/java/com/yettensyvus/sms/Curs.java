package com.yettensyvus.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
public class Curs {
    private String denumire;
    private int credite;
    private Profesor profesor;

    // Constructor for constructor injectio
    @Autowired
    public Curs(String denumire, int credite, Profesor profesor) {
        this.denumire = denumire;
        this.credite = credite;
        this.profesor = profesor;
    }

    // Setter for profesor
    @Autowired
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
}
