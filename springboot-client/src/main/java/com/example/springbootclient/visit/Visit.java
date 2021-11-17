package com.example.springbootclient.visit;

import com.example.springbootclient.physician.Physician;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String patientFirstName;
    private String patientLastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateVisit;
    private LocalTime hourVisit;

    @ManyToOne
    @JoinColumn(name = "physician_id")
    private Physician physician;

    @Builder(toBuilder = true)
    public Visit(Long id, String patientFirstName, String patientLastName, LocalDate dateVisit, LocalTime hourVisit, Physician physician) {
        this.id = id;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.dateVisit = dateVisit;
        this.hourVisit = hourVisit;
        this.physician = physician;
    }

    public Visit() {
    }
}
