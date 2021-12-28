package com.example.jfxclient.controller;

import com.example.jfxclient.dto.PhysicianDto;
import com.example.jfxclient.popup.InfoPopup;
import com.example.jfxclient.rest.PhysicianRestClient;
import com.example.jfxclient.validator.PhysicianDtoValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPhysiciansController implements Initializable {

    @FXML
    private Pane addPhysicianPane;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField physicianFirstName;

    @FXML
    private TextField physicianLastName;

    @FXML
    private TextField physicianSpecialization;

    private final PhysicianRestClient PHYSICIAN_REST_CLIENT;
    private final InfoPopup POPUP;
    private PhysicianDtoValidator validator;

    public AddPhysiciansController() {
        this.PHYSICIAN_REST_CLIENT = PhysicianRestClient.getInstance();
        this.POPUP = new InfoPopup();
        this.validator = new PhysicianDtoValidator();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeAddButton();
    }

    private void initializeAddButton() {
        addButton.setOnAction((x)-> {
            PhysicianDto physicianDto = getPhysicianDto();

            if(validator.firstLastNameValidator(physicianDto)){
                Stage infoPopup = POPUP.createInfoPopup("Error!!! First or last patient name is not correct", () -> {
                });
                infoPopup.show();
            } else if(validator.specializationValidator(physicianDto)){
                Stage infoPopup = POPUP.createInfoPopup("Error!!! Specialization is required", () -> {
                });
                infoPopup.show();
            } else {
                PHYSICIAN_REST_CLIENT.savePhysician(physicianDto, ()->{
                    Stage infoPopup = POPUP.createInfoPopup("Physician has been saved", () -> {
                    });
                    getStage().close();
                    infoPopup.show();
                });
            }
        });
    }

    private PhysicianDto getPhysicianDto() {
        PhysicianDto physicianDto = new PhysicianDto();
        physicianDto.setFirstName(physicianFirstName.getText());
        physicianDto.setLastName(physicianLastName.getText());
        physicianDto.setSpecialization(physicianSpecialization.getText());
        return physicianDto;
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction((x)->{
            getStage().close();
        });
    }

    private Stage getStage() {
        return (Stage) addPhysicianPane.getScene().getWindow();
    }


}
