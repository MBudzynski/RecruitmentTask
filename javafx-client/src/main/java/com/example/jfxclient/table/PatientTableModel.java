package com.example.jfxclient.table;

import com.example.jfxclient.dto.VisitDto;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import java.time.LocalTime;

public class PatientTableModel {

    private final SimpleLongProperty id;
    private final SimpleStringProperty patientFirstName;
    private final SimpleStringProperty patientLastName;
    private final SimpleStringProperty dateVisit;
    private final SimpleStringProperty hourVisit;

    public PatientTableModel(Long id,
                             String patientFirstName,
                             String patientLastName,
                             LocalDate dateVisit,
                             LocalTime hourVisit) {
        this.id = new SimpleLongProperty(id);
        this.patientFirstName = new SimpleStringProperty(patientFirstName);
        this.patientLastName = new SimpleStringProperty(patientLastName);
        this.dateVisit = new SimpleStringProperty(dateVisit.toString());
        this.hourVisit = new SimpleStringProperty(hourVisit.toString());
    }

    public static PatientTableModel fromDto(VisitDto visitDto){
        return new PatientTableModel(visitDto.getId(), visitDto.getPatientFirstName(),
                visitDto.getPatientLastName(), visitDto.getDateVisit(), visitDto.getHourVisit());
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getPatientFirstName() {
        return patientFirstName.get();
    }

    public SimpleStringProperty patientFirstNameProperty() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName.set(patientFirstName);
    }

    public String getPatientLastName() {
        return patientLastName.get();
    }

    public SimpleStringProperty patientLastNameProperty() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName.set(patientLastName);
    }

    public String getDateVisit() {
        return dateVisit.get();
    }

    public SimpleStringProperty dateVisitProperty() {
        return dateVisit;
    }

    public void setDateVisit(String dateVisit) {
        this.dateVisit.set(dateVisit);
    }

    public String getHourVisit() {
        return hourVisit.get();
    }

    public SimpleStringProperty hourVisitProperty() {
        return hourVisit;
    }

    public void setHourVisit(String hourVisit) {
        this.hourVisit.set(hourVisit);
    }
}
