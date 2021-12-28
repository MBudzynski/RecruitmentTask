package com.example.jfxclient.controller;


import com.example.jfxclient.dbupdateobserver.Observer;
import com.example.jfxclient.dto.PhysicianDto;
import com.example.jfxclient.dto.PhysicianIdHolder;
import com.example.jfxclient.dto.VisitDto;
import com.example.jfxclient.rest.PhysicianRestClient;
import com.example.jfxclient.rest.VisitsRestClient;
import com.example.jfxclient.table.PatientTableModel;
import com.example.jfxclient.table.PhysiciansTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppController implements Initializable, Observer {

    private final PhysicianRestClient PHYSICIAN_REST_CLIENT;
    private final VisitsRestClient VISITS_REST_CLIENT;
    private ObservableList<PatientTableModel> dateVisits;
    private ObservableList<PhysiciansTableModel> date;
    private final PhysicianIdHolder PHYSICIAN_ID_HOLDER;


    @FXML
    private TableView<PhysiciansTableModel> physicians;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;

    @FXML
    private TableView<PatientTableModel> patientVisits;

    @FXML
    private TextField searchField;

    @FXML
    void selectedRows(MouseEvent event) {
        Thread thread = new Thread(() -> {
            loadPhysicianVisits(physicians.getSelectionModel().getSelectedItem().getID());
        });
        thread.start();
    }


    @FXML
    void uncheckSelectedRows(MouseEvent event) {
        physicians.getSelectionModel().clearSelection();
        patientVisits.getSelectionModel().clearSelection();
        dateVisits.clear();
    }


    public AppController() {
        this.date = FXCollections.observableArrayList();
        this.dateVisits = FXCollections.observableArrayList();
        this.PHYSICIAN_REST_CLIENT = PhysicianRestClient.getInstance();
        this.VISITS_REST_CLIENT = VisitsRestClient.getInstance();
        this.PHYSICIAN_ID_HOLDER = PhysicianIdHolder.getInstance();
        PHYSICIAN_REST_CLIENT.register(this);
        VISITS_REST_CLIENT.register(this);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePhysiciansTable();
        initializePatientVisitsTable();
        initializeDeleteButton();
        initializeAddButton();
    }


    private void initializeAddButton() {
        addButton.setOnAction((x) -> {
            PhysiciansTableModel selectedPhysician = physicians.getSelectionModel().getSelectedItem();
            if (selectedPhysician != null) {
                try {
                    prepareStageToAddVisit(selectedPhysician);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    prepareStageToAddPhysician();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void prepareStageToAddPhysician() throws IOException {
        Stage addPhysicianStage = new Stage();
        addPhysicianStage.initModality(Modality.APPLICATION_MODAL);
        Parent addPhysicianParent = FXMLLoader.load(getClass().getResource("/fxml/addPhysician.fxml"));
        Scene scene = new Scene(addPhysicianParent, 500, 400);
        addPhysicianStage.setScene(scene);
        addPhysicianStage.show();
    }

    private void prepareStageToAddVisit(PhysiciansTableModel selectedPhysician) throws IOException {
        Stage addVisitToPhysicianStage = new Stage();
        addVisitToPhysicianStage.initModality(Modality.APPLICATION_MODAL);
        Parent loader = FXMLLoader
                .load(getClass().getResource("/fxml/addVisitToPhysician.fxml"));
        PHYSICIAN_ID_HOLDER.setPhysicianId(selectedPhysician.getID());
        Scene scene = new Scene(loader, 470, 450);
        addVisitToPhysicianStage.setScene(scene);
        addVisitToPhysicianStage.show();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(x -> {
            PatientTableModel selectedPatientVisit = patientVisits.getSelectionModel().getSelectedItem();
            PhysiciansTableModel selectedPhysician = physicians.getSelectionModel().getSelectedItem();
            if (selectedPatientVisit != null) {
                VISITS_REST_CLIENT.deleteRecord(selectedPatientVisit.getID());
                patientVisits.getItems().remove(selectedPatientVisit);
            } else if (selectedPhysician != null) {
                PHYSICIAN_REST_CLIENT.deleteRecord(selectedPhysician.getID());
                loadPhysiciansData();
            }
        });
    }

    private void initializePhysiciansTable() {
        physicians.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn idColumn = new TableColumn("Id");
        idColumn.setMinWidth(30);
        idColumn.setCellValueFactory(new PropertyValueFactory<PhysiciansTableModel, Long>("ID"));

        TableColumn firstNameColumn = new TableColumn("First Name");
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<PhysiciansTableModel, String>("FIRST_NAME"));

        TableColumn lastNameColumn = new TableColumn("Last Name");
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<PhysiciansTableModel, String>("LAST_NAME"));

        TableColumn specializeColumn = new TableColumn("Specialize");
        specializeColumn.setMinWidth(100);
        specializeColumn.setCellValueFactory(new PropertyValueFactory<PhysiciansTableModel, String>("SPECIALIZATION"));

        physicians.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, specializeColumn);

        loadPhysiciansData();

        physicians.setItems(date);

        physicians.setItems(sortPhysicianList());
    }

    private SortedList<PhysiciansTableModel> sortPhysicianList() {
        FilteredList<PhysiciansTableModel> filteredData = new FilteredList<>(date, b -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(physiciansSearchModel -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchText = newValue.trim().toLowerCase();
                if (physiciansSearchModel.getFIRST_NAME().toLowerCase().indexOf(searchText) > -1) {
                    return true;
                } else if (physiciansSearchModel.getLAST_NAME().toLowerCase().indexOf(searchText) > -1) {
                    return true;
                } else if (physiciansSearchModel.getSPECIALIZATION().toLowerCase().indexOf(searchText) > -1) {
                    return true;
                } else
                    return false;
            });
        });
        SortedList<PhysiciansTableModel> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(physicians.comparatorProperty());
        return sortedList;
    }

    private void initializePatientVisitsTable() {
        patientVisits.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn idColumnVisits = new TableColumn("Id");
        idColumnVisits.setMinWidth(30);
        idColumnVisits.setCellValueFactory(new PropertyValueFactory<PatientTableModel, Long>("ID"));

        TableColumn firstNameColumnVisits = new TableColumn("Patient First Name");
        firstNameColumnVisits.setMinWidth(100);
        firstNameColumnVisits.setCellValueFactory(new PropertyValueFactory<PatientTableModel, String>("PATIENT_FIRST_NAME"));

        TableColumn lastNameColumnVisits = new TableColumn("Patient Last Name");
        lastNameColumnVisits.setMinWidth(100);
        lastNameColumnVisits.setCellValueFactory(new PropertyValueFactory<PatientTableModel, String>("PATIENT_LAST_NAME"));

        TableColumn dateVisitColumn = new TableColumn("Date Visit");
        dateVisitColumn.setMinWidth(100);
        dateVisitColumn.setCellValueFactory(new PropertyValueFactory<PatientTableModel, String>("DATE_VISIT"));

        TableColumn hourVisitColumn = new TableColumn("Hour Visit");
        hourVisitColumn.setMinWidth(100);
        hourVisitColumn.setCellValueFactory(new PropertyValueFactory<PatientTableModel, String>("HOUR_VISIT"));

        patientVisits.getColumns().addAll(idColumnVisits, firstNameColumnVisits, lastNameColumnVisits, dateVisitColumn, hourVisitColumn);

        patientVisits.setItems(dateVisits);
    }

    private void loadPhysicianVisits(Long physicianId) {
        List<VisitDto> physicianVisits = PHYSICIAN_REST_CLIENT
                .getPhysicianVisits(physicianId);
        dateVisits.clear();
        dateVisits.addAll(physicianVisits.stream().map(PatientTableModel::fromDto).collect(Collectors.toList()));
    }

    private void loadPhysiciansData() {
        Thread thread = new Thread(() -> {
            List<PhysicianDto> physicians = PHYSICIAN_REST_CLIENT.getPhysicians();
            date.clear();
            date.addAll(physicians.stream().map(PhysiciansTableModel::fromDto).collect(Collectors.toList()));
        });
        thread.start();
    }

    @Override
    public void loadPhysicians() {
        loadPhysiciansData();
    }

    @Override
    public void loadVisits() {
        loadPhysicianVisits(PHYSICIAN_ID_HOLDER.getPhysicianId());
        PHYSICIAN_ID_HOLDER.setPhysicianId(-1L);
    }

}
