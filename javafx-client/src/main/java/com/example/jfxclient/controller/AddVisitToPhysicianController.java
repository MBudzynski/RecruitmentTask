package com.example.jfxclient.controller;

import com.example.jfxclient.dto.PhysicianIdHolder;
import com.example.jfxclient.dto.VisitDto;
import com.example.jfxclient.popup.PopupOk;
import com.example.jfxclient.rest.VisitsRestClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddVisitToPhysicianController implements Initializable {


    @FXML
    private Button addButton;

    @FXML
    private Pane addVisitToPhysicianPane;

    @FXML
    private Button cancelButton;

    @FXML
    private Spinner<Integer> hourVisit;

    @FXML
    private Spinner<Integer> minuteVisit;

    @FXML
    private TextField patientFirstName;

    @FXML
    private TextField patientLastName;

    @FXML
    private DatePicker dataVisit;

    SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24,12);;
    SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,60,30);


    private final VisitsRestClient visitsRestClient;
    private final PopupOk popupOk;


    public AddVisitToPhysicianController() {
        this.visitsRestClient = new VisitsRestClient();
        this.popupOk = new PopupOk();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeAddButton();
        hourVisit.setValueFactory(hourValueFactory);
        minuteVisit.setValueFactory(minuteValueFactory);
    }

    private Stage getStage() {
        return (Stage) addVisitToPhysicianPane.getScene().getWindow();
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction((x)->{
            getStage().close();
        });
    }

    private void initializeAddButton() {
        addButton.setOnAction((x)-> {
           VisitDto visitDto = getVisitDto();
            PhysicianIdHolder holder = PhysicianIdHolder.getInstance();
            visitDto.setId(holder.getPhysicianId());
            visitsRestClient.saveVisit(visitDto, ()->{
                Stage infoPopup = popupOk.createOkPopup("Visit has been saved", () -> {
                });
                getStage().close();
                infoPopup.show();
            });
        });
    }

    private VisitDto getVisitDto() {
        VisitDto visitDto = new VisitDto();
        visitDto.setPatientFirstName(patientFirstName.getText());
        visitDto.setPatientLastName(patientLastName.getText());
        visitDto.setDateVisit(dataVisit.getValue());
        visitDto.setHourVisit(LocalTime.of(hourVisit.getValue(),minuteVisit.getValue()));
        return visitDto;
    }


}
