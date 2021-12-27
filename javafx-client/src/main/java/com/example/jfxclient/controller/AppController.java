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

    private final PhysicianRestClient physicianRestClient;
    private final VisitsRestClient visitsRestClient;
    private ObservableList<PatientTableModel> dateVisits;
    private ObservableList<PhysiciansTableModel> date;
    private final PhysicianIdHolder physicianIdHolder;


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
            loadPhysicianVisits(physicians.getSelectionModel().getSelectedItem().getId());
        });
        thread.start();
    }

    private void loadPhysicianVisits(Long physicianId) {
        List<VisitDto> physicianVisits = physicianRestClient
                .getPhysicianVisits(physicianId);
        dateVisits.clear();
        dateVisits.addAll(physicianVisits.stream().map(PatientTableModel::fromDto).collect(Collectors.toList()));
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
        this.physicianRestClient = PhysicianRestClient.getInstance();
        this.visitsRestClient = VisitsRestClient.getInstance();
        this.physicianIdHolder = PhysicianIdHolder.getInstance();
        physicianRestClient.register(this);
        visitsRestClient.register(this);
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
        physicianIdHolder.setPhysicianId(selectedPhysician.getId());
        Scene scene = new Scene(loader, 470, 450);
        addVisitToPhysicianStage.setScene(scene);
        addVisitToPhysicianStage.show();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(x -> {
            PatientTableModel selectedPatientVisit = patientVisits.getSelectionModel().getSelectedItem();
            PhysiciansTableModel selectedPhysician = physicians.getSelectionModel().getSelectedItem();
            if (selectedPatientVisit != null) {
                visitsRestClient.deleteRecord(selectedPatientVisit.getId());
                patientVisits.getItems().remove(selectedPatientVisit);
            } else if (selectedPhysician != null) {
                physicianRestClient.deleteRecord(selectedPhysician.getId());
                loadPhysiciansData();
            }
        });
    }

    private void initializePhysiciansTable() {
        physicians.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn idColumn = new TableColumn("Id");
        idColumn.setMinWidth(30);
        idColumn.setCellValueFactory(new PropertyValueFactory<PhysiciansTableModel, Long>("id"));

        TableColumn firstNameColumn = new TableColumn("First Name");
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<PhysiciansTableModel, String>("firstName"));

        TableColumn lastNameColumn = new TableColumn("Last Name");
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<PhysiciansTableModel, String>("lastName"));

        TableColumn specializeColumn = new TableColumn("Specialize");
        specializeColumn.setMinWidth(100);
        specializeColumn.setCellValueFactory(new PropertyValueFactory<PhysiciansTableModel, String>("specialization"));

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
                if (physiciansSearchModel.getFirstName().toLowerCase().indexOf(searchText) > -1) {
                    return true;
                } else if (physiciansSearchModel.getLastName().toLowerCase().indexOf(searchText) > -1) {
                    return true;
                } else if (physiciansSearchModel.getSpecialization().toLowerCase().indexOf(searchText) > -1) {
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
        idColumnVisits.setCellValueFactory(new PropertyValueFactory<PatientTableModel, Long>("id"));

        TableColumn firstNameColumnVisits = new TableColumn("Patient First Name");
        firstNameColumnVisits.setMinWidth(100);
        firstNameColumnVisits.setCellValueFactory(new PropertyValueFactory<PatientTableModel, String>("patientFirstName"));

        TableColumn lastNameColumnVisits = new TableColumn("Patient Last Name");
        lastNameColumnVisits.setMinWidth(100);
        lastNameColumnVisits.setCellValueFactory(new PropertyValueFactory<PatientTableModel, String>("patientLastName"));

        TableColumn dateVisitColumn = new TableColumn("Date Visit");
        dateVisitColumn.setMinWidth(100);
        dateVisitColumn.setCellValueFactory(new PropertyValueFactory<PatientTableModel, String>("dateVisit"));

        TableColumn hourVisitColumn = new TableColumn("Hour Visit");
        hourVisitColumn.setMinWidth(100);
        hourVisitColumn.setCellValueFactory(new PropertyValueFactory<PatientTableModel, String>("hourVisit"));

        patientVisits.getColumns().addAll(idColumnVisits, firstNameColumnVisits, lastNameColumnVisits, dateVisitColumn, hourVisitColumn);

        patientVisits.setItems(dateVisits);
    }

    private void loadPhysiciansData() {
        Thread thread = new Thread(() -> {
            List<PhysicianDto> physicians = physicianRestClient.getPhysicians();
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
        loadPhysicianVisits(physicianIdHolder.getPhysicianId());
        physicianIdHolder.setPhysicianId(-1L);
    }

}
