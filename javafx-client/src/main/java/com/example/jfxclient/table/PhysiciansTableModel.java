package com.example.jfxclient.table;

import com.example.jfxclient.dto.PhysicianDto;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;


public class PhysiciansTableModel {

    private final SimpleLongProperty ID;
    private final SimpleStringProperty FIRST_NAME;
    private final SimpleStringProperty LAST_NAME;
    private final SimpleStringProperty SPECIALIZATION;

    public PhysiciansTableModel(Long id,
                                String firstName, String lastName, String specialization) {
        this.ID = new SimpleLongProperty(id);
        this.FIRST_NAME = new SimpleStringProperty(firstName);
        this.LAST_NAME = new SimpleStringProperty(lastName);
        this.SPECIALIZATION = new SimpleStringProperty(specialization);
    }

    public static PhysiciansTableModel fromDto(PhysicianDto physicianDto){
        return new PhysiciansTableModel(physicianDto.getId(), physicianDto.getFirstName(), physicianDto.getLastName(), physicianDto.getSpecialization());
    }

    public String getFIRST_NAME() {
        return FIRST_NAME.get();
    }

    public SimpleStringProperty FIRST_NAMEProperty() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME.set(FIRST_NAME);
    }

    public String getLAST_NAME() {
        return LAST_NAME.get();
    }

    public SimpleStringProperty LAST_NAMEProperty() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME.set(LAST_NAME);
    }

    public String getSPECIALIZATION() {
        return SPECIALIZATION.get();
    }

    public SimpleStringProperty SPECIALIZATIONProperty() {
        return SPECIALIZATION;
    }

    public void setSPECIALIZATION(String SPECIALIZATION) {
        this.SPECIALIZATION.set(SPECIALIZATION);
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
}
