package com.example.jfxclient.validator;

import com.example.jfxclient.dto.VisitDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class VisitDtoValidator {

    public boolean dateValidator(VisitDto dto){
        if(dto.getDateVisit().isBefore(LocalDate.now())){
            return true;
        } else if(dto.getDateVisit().equals(LocalDate.now()) && dto.getHourVisit().isBefore(LocalTime.now())){
            return true;
        } else {
            return false;
        }
    }

    public boolean firstLastNameValidator(VisitDto dto){
        if(dto.getPatientFirstName().isBlank() || !dto.getPatientFirstName().matches("[A-ZŁ][a-złęąćżźóń ]{3,}")){
            return true;
        } else if(dto.getPatientLastName().isBlank() || !dto.getPatientLastName().matches("[A-ZŁ][a-złęąćżźóń ]{3,}")){
            return true;
        }else {
            return false;
        }
    }

}
