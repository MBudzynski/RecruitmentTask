package com.example.springbootclient.physician;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PhysicianDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;


    @Builder(toBuilder = true)
    public PhysicianDto(Long id, String firstName, String lastName, String specialization) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;

    }

    public Physician fromDto(){
        return Physician.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .specialization(specialization)
                .build();
    }

    public static PhysicianDto toDto(Physician physician){
        return PhysicianDto.builder()
                .id(physician.getId())
                .firstName(physician.getFirstName())
                .lastName(physician.getLastName())
                .specialization(physician.getSpecialization())
                .build();
    }

}
