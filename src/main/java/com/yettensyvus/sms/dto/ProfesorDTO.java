// ProfesorDTO.java
package com.yettensyvus.sms.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfesorDTO {
    private Long id;
    private String nume;
    private String materie;
    private int experientaAni;
    private List<Long> cursuriIds = new ArrayList<>();
}