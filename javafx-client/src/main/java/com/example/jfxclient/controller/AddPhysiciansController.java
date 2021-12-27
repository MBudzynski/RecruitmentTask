package com.example.jfxclient.controller;

import com.example.jfxclient.dto.PhysicianDto;
import com.example.jfxclient.popup.InfoPopup;
import com.example.jfxclient.rest.PhysicianRestClient;
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

    private final PhysicianRestClient physicianRestClient;
    private final InfoPopup popup;

    public AddPhysiciansController() {
        this.physicianRestClient = PhysicianRestClient.getInstance();
        this.popup = new InfoPopup();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeAddButton();
    }

    private void initializeAddButton() {
        addButton.setOnAction((x)-> {
            PhysicianDto physicianDto = getPhysicianDto();
            physicianRestClient.savePhysician(physicianDto, ()->{
                    Stage infoPopup = popup.createInfoPopup("Physician has been saved", () -> {
                    });
                    getStage().close();
                    infoPopup.show();
                });

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
