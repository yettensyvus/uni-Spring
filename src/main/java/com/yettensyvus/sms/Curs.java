package com.yettensyvus.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curs {
    private String denumire;
    private int credite;
    private Profesor profesor;

    // Setter for profesor (this will be used for setter injection)
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
}
