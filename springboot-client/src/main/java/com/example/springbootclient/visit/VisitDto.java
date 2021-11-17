package com.example.springbootclient.visit;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class VisitDto {

    private Long id;
    private String patientFirstName;
    private String patientLastName;
    private LocalDate dateVisit;
    private LocalTime hourVisit;

    @Builder(toBuilder = true)
    public VisitDto(Long id, String patientFirstName, String patientLastName, LocalDate dateVisit, LocalTime hourVisit) {
        this.id = id;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.dateVisit = dateVisit;
        this.hourVisit = hourVisit;
    }

    public Visit fromDto(){
        return Visit.builder()
                .id(id)
                .patientFirstName(patientFirstName)
                .patientLastName(patientLastName)
                .dateVisit(dateVisit)
                .hourVisit(hourVisit)
                .build();
    }

    public static VisitDto toDto(Visit visit){
        return VisitDto.builder()
                .id(visit.getId())
                .patientFirstName(visit.getPatientFirstName())
                .patientLastName(visit.getPatientLastName())
                .dateVisit(visit.getDateVisit())
                .hourVisit(visit.getHourVisit())
                .build();
    }

    public static List<VisitDto> toDtoList(List<Visit> visitList) {
        return visitList.stream().map(VisitDto::toDto).collect(Collectors.toList());
    }

    public static List<Visit> fromDtoList(List<VisitDto> visitDtoList) {
        return Objects.isNull(visitDtoList)
                ? List.of()
                : visitDtoList.stream().map(VisitDto::fromDto).collect(Collectors.toList());
    }

}
