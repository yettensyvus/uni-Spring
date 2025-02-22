package com.yettensyvus.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {
    private String nume;
    private String materie;
    private int experientaAni;
}
