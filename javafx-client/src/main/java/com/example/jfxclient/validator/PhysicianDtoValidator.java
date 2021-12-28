package com.example.jfxclient.validator;

import com.example.jfxclient.dto.PhysicianDto;

public class PhysicianDtoValidator {

    public boolean firstLastNameValidator(PhysicianDto dto){
        if(dto.getFirstName().isBlank() || !dto.getFirstName().matches("[A-ZŁ][a-złęąćżźóń ]{3,}")){
            return true;
        } else if(dto.getFirstName().isBlank() || !dto.getFirstName().matches("[A-ZŁ][a-złęąćżźóń ]{3,}")){
            return true;
        }else {
            return false;
        }
    }

    public boolean specializationValidator(PhysicianDto dto) {
        if(dto.getSpecialization().isBlank() || !dto.getSpecialization().matches("[A-ZŁ][a-złęąćżźóń ]{3,}")){
            return true;
        }else {
            return false;
        }
    }
}
