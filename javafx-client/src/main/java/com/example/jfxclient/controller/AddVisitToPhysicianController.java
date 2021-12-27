package com.example.jfxclient.controller;

import com.example.jfxclient.dto.VisitDto;
import com.example.jfxclient.popup.InfoPopup;
import com.example.jfxclient.rest.VisitsRestClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
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
    private final InfoPopup popup;


    public AddVisitToPhysicianController() {
        this.visitsRestClient = VisitsRestClient.getInstance();
        this.popup = new InfoPopup();
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
           if(dateValidator(visitDto)){
                visitsRestClient.saveVisit(visitDto, ()->{
                    Stage infoPopup = popup.createInfoPopup("Visit has been saved", () -> {
                    });
                    getStage().close();
                    infoPopup.show();
                });
            } else {
               Stage infoPopup = popup.createInfoPopup("Error!!! Visit date is expired", () -> {
               });
               getStage().close();
               infoPopup.show();
            }
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

    private boolean dateValidator(VisitDto dto){
        if(dto.getDateVisit().isAfter(LocalDate.now())){
            return true;
        } else if(dto.getDateVisit().equals(LocalDate.now()) && dto.getHourVisit().isAfter(LocalTime.now())){
            return true;
        } else {
            return false;
        }
    }


}
