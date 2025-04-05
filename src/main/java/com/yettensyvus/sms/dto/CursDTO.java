// CursDTO.java
package com.yettensyvus.sms.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CursDTO {
    private Long id;
    private String denumire;
    private int credite;
    private Long profesorId;
    private List<Long> studentiIds = new ArrayList<>();
}