package com.example.springbootclient.physician;

import com.example.springbootclient.visit.Visit;
import com.example.springbootclient.visit.VisitDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Physician {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;

    @OneToMany(mappedBy = "physician",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Visit> visitList = new ArrayList<>();

    @Builder(toBuilder = true)
    public Physician(Long id, String firstName, String lastName, String specialization, List<Visit> visitList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.visitList = visitList;
    }

    public Physician() {
    }

    public Visit addVisit(Visit visit) {
        visitList.add(visit);
        return visit;
    }
}
