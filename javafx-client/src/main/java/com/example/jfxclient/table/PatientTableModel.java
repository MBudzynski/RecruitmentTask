package com.example.jfxclient.table;

import com.example.jfxclient.dto.VisitDto;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import java.time.LocalTime;

public class PatientTableModel {

    private final SimpleLongProperty ID;
    private final SimpleStringProperty PATIENT_FIRST_NAME;
    private final SimpleStringProperty PATIENT_LAST_NAME;
    private final SimpleStringProperty DATE_VISIT;
    private final SimpleStringProperty HOUR_VISIT;

    public PatientTableModel(Long id,
                             String patientFirstName,
                             String patientLastName,
                             LocalDate dateVisit,
                             LocalTime hourVisit) {
        this.ID = new SimpleLongProperty(id);
        this.PATIENT_FIRST_NAME = new SimpleStringProperty(patientFirstName);
        this.PATIENT_LAST_NAME = new SimpleStringProperty(patientLastName);
        this.DATE_VISIT = new SimpleStringProperty(dateVisit.toString());
        this.HOUR_VISIT = new SimpleStringProperty(hourVisit.toString());
    }

    public static PatientTableModel fromDto(VisitDto visitDto){
        return new PatientTableModel(visitDto.getId(), visitDto.getPatientFirstName(),
                visitDto.getPatientLastName(), visitDto.getDateVisit(), visitDto.getHourVisit());
    }

    public long getID() {
        return ID.get();
    }

    public SimpleLongProperty IDProperty() {
        return ID;
    }

    public void setID(long ID) {
        this.ID.set(ID);
    }

    public String getPATIENT_FIRST_NAME() {
        return PATIENT_FIRST_NAME.get();
    }

    public SimpleStringProperty PATIENT_FIRST_NAMEProperty() {
        return PATIENT_FIRST_NAME;
    }

    public void setPATIENT_FIRST_NAME(String PATIENT_FIRST_NAME) {
        this.PATIENT_FIRST_NAME.set(PATIENT_FIRST_NAME);
    }

    public String getPATIENT_LAST_NAME() {
        return PATIENT_LAST_NAME.get();
    }

    public SimpleStringProperty PATIENT_LAST_NAMEProperty() {
        return PATIENT_LAST_NAME;
    }

    public void setPATIENT_LAST_NAME(String PATIENT_LAST_NAME) {
        this.PATIENT_LAST_NAME.set(PATIENT_LAST_NAME);
    }

    public String getDATE_VISIT() {
        return DATE_VISIT.get();
    }

    public SimpleStringProperty DATE_VISITProperty() {
        return DATE_VISIT;
    }

    public void setDATE_VISIT(String DATE_VISIT) {
        this.DATE_VISIT.set(DATE_VISIT);
    }

    public String getHOUR_VISIT() {
        return HOUR_VISIT.get();
    }

    public SimpleStringProperty HOUR_VISITProperty() {
        return HOUR_VISIT;
    }

    public void setHOUR_VISIT(String HOUR_VISIT) {
        this.HOUR_VISIT.set(HOUR_VISIT);
    }
}
