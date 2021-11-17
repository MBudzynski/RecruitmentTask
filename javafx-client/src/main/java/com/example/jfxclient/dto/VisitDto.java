package com.example.jfxclient.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VisitDto {

    private Long id;
    private String patientFirstName;
    private String patientLastName;
    private LocalDate dateVisit;
    private LocalTime hourVisit;

}
