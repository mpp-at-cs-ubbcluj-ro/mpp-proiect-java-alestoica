package controller;

import dto.AgeEventDTO;
import dto.DTOUtils;
import dto.ParticipantDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AgeEvent;
import model.Employee;
import model.Participant;
import model.Registration;
import service.IObserver;
import service.IService;

import java.io.IOException;
import java.util.*;

public class AccountController implements IObserver {
    IService service;
    @FXML
    Label welcomeLabel;
    ObservableList<AgeEventDTO> modelAgeEvent = FXCollections.observableArrayList();
    @FXML
    TableView<AgeEventDTO> tableViewEvents;
    ObservableList<ParticipantDTO> modelParticipants = FXCollections.observableArrayList();
    @FXML
    TableView<ParticipantDTO> tableViewParticipants;
    @FXML
    TableColumn<AgeEvent, String> tableColumnAgeGroup;
    @FXML
    TableColumn<AgeEvent, String> tableColumnSportsEvent;
    @FXML
    TableColumn<AgeEvent, Integer> tableColumnNoParticipants;
    @FXML
    TableColumn<Participant, String> tableColumnFullName;
    @FXML
    TableColumn<Participant, Integer> tableColumnAge;
    @FXML
    TableColumn<Participant, Integer> tableColumnNoRegistrations;
    Employee currentEmployee;
    Stage dialogStage;

    public void setService(IService service, Employee employee, Stage dialogStage) {
        this.service = service;

        this.currentEmployee = employee;
        this.dialogStage = dialogStage;
        this.welcomeLabel.setText("Welcome, " + currentEmployee.getFirstName() + "!");

        initModelAgeEvents();
        initModelParticipants();
    }

    private void initModelAgeEvents() {
//        System.out.println("initModel");
        modelAgeEvent.clear();
        Collection<AgeEvent> ageEvents = service.getAllAgeEvents();
        Collection<AgeEventDTO> ageEventsDTO = new ArrayList<>();
        ageEvents.forEach(ageEvent -> {
            AgeEventDTO ageEventDTO = DTOUtils.getDTO(ageEvent);
            ageEventDTO.setNoParticipants(service.countParticipants(ageEvent));
            ageEventsDTO.add(ageEventDTO);
        });
        modelAgeEvent.setAll(ageEventsDTO);
    }

    private void initModelParticipants() {
//        System.out.println("initModel1");
        modelParticipants.clear();
//        System.out.println("initModel2");
        Collection<Participant> participants = service.getAllParticipants();
//        System.out.println("initModel3");
        Collection<ParticipantDTO> participantsDTO = new ArrayList<>();
        participants.forEach(participant -> {
            ParticipantDTO participantDTO = DTOUtils.getDTO(participant);
            participantDTO.setNoRegistrations(service.countRegistrations(participant));
            participantsDTO.add(participantDTO);
        });
//        System.out.println("initModel4");
        modelParticipants.setAll(participantsDTO);
//        System.out.println("initModel5");
    }

    @FXML
    private void initialize() {
        tableColumnAgeGroup.setCellValueFactory(new PropertyValueFactory<>("ageGroup"));
        tableColumnSportsEvent.setCellValueFactory(new PropertyValueFactory<>("sportsEvent"));
//        tableColumnNoParticipants.setCellValueFactory(cellData -> {
//            AgeEvent ageEvent = cellData.getValue();
//            int noParticipants = service.countParticipants(ageEvent);
//            return new SimpleIntegerProperty(noParticipants).asObject();
//        });
        tableColumnNoParticipants.setCellValueFactory(new PropertyValueFactory<>("noParticipants"));

        tableViewEvents.setItems(modelAgeEvent);

        tableColumnFullName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
//        tableColumnNoRegistrations.setCellValueFactory(cellData -> {
//            Participant participant = cellData.getValue();
//            int noRegistrations = service.countRegistrations(participant);
//            return new SimpleIntegerProperty(noRegistrations).asObject();
//        });
        tableColumnNoRegistrations.setCellValueFactory(new PropertyValueFactory<>("noRegistrations"));

        tableViewParticipants.setItems(modelParticipants);
    }

    @FXML
    private void handleSearch() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/search-view.fxml"));
            AnchorPane layout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Search");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(layout));

            AgeEventDTO ageEventDTO = tableViewEvents.getSelectionModel().getSelectedItem();
            AgeEvent ageEvent = null;
            if (ageEventDTO != null)
                ageEvent = DTOUtils.getFromDTO(ageEventDTO);

            if (ageEvent == null)
                MessageAlert.showErrorMessage(null, "No event was selected!");
            else {
                SearchController searchController = loader.getController();

                searchController.setService(service, ageEvent);

                dialogStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/register-view.fxml"));
            AnchorPane layout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Register");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(layout));

            ParticipantDTO currentParticipantDTO = tableViewParticipants.getSelectionModel().getSelectedItem();
            Participant currentParticipant = null;
            if (currentParticipantDTO != null)
                currentParticipant = DTOUtils.getFromDTO(currentParticipantDTO);


            if (currentParticipant != null && service.findByParticipant(currentParticipant).size() == 2)
                MessageAlert.showErrorMessage(null, "This participant is already registered in the maximum number of events (2)!");
            else {
                RegisterController registerController = loader.getController();

                registerController.setService(service, currentParticipant, currentEmployee, dialogStage);

                dialogStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogOut() {
        try {
            service.logout(currentEmployee.getId(), this);
        } catch (Exception e) {
            System.out.println("Logout error " + e);
        }
        dialogStage.close();
    }

//    private int countParticipants(AgeEvent ageEvent) {
//        Iterable<Registration> registrations = service.findByAgeEvent(ageEvent);
//        Map<Long, Integer> participantsCountMap = new HashMap<>();
//
//        for (Registration registration : registrations) {
//            Long eventId = registration.getAgeEvent().getId();
//            participantsCountMap.put(eventId, participantsCountMap.getOrDefault(eventId, 0) + 1);
//        }
//
//        return participantsCountMap.getOrDefault(ageEvent.getId(), 0);
//    }
//
//    private int countRegistrations(Participant participant) {
//        Collection<Registration> registrations = service.findByParticipant(participant);
//        return registrations.size();
//    }

    @Override
    public void notifyAddRegistration(Registration registration) throws Exception {
        System.out.println("account controller notify reg");
//        modelParticipants.clear();
        initModelParticipants();
//        initModelAgeEvents();
//        tableViewEvents.setItems(modelAgeEvent);
//        initialize();
//        tableViewParticipants.setItems(modelParticipants);
    }

    @Override
    public void notifyAddParticipant(Participant participant) throws Exception {
        System.out.println("account controller notify part");
//        initModelParticipants();
//        initialize();
//        tableViewParticipants.setItems(modelParticipants);
    }
}
