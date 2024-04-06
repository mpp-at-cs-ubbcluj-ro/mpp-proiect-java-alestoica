package controller;

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
import service.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountController {
    Service service;
    @FXML
    Label welcomeLabel;
    ObservableList<AgeEvent> modelAgeEvent = FXCollections.observableArrayList();
    @FXML
    TableView<AgeEvent> tableViewEvents;
    ObservableList<Participant> modelParticipants = FXCollections.observableArrayList();
    @FXML
    TableView<Participant> tableViewParticipants;
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

    public void setService(Service service, Employee employee) {
        this.service = service;

        this.currentEmployee = employee;
        this.welcomeLabel.setText("Welcome, " + currentEmployee.getFirstName() + "!");

        initModel();
    }

    private void initModel() {
        Collection<AgeEvent> ageEvents = service.getAllAgeEvents();
        modelAgeEvent.setAll(ageEvents);

        Collection<Participant> participants = service.getAllParticipants();
        modelParticipants.setAll(participants);
    }

    @FXML
    private void initialize() {
        tableColumnAgeGroup.setCellValueFactory(new PropertyValueFactory<>("ageGroup"));
        tableColumnSportsEvent.setCellValueFactory(new PropertyValueFactory<>("sportsEvent"));
        tableColumnNoParticipants.setCellValueFactory(cellData -> {
            AgeEvent ageEvent = cellData.getValue();
            int noParticipants = countParticipants(ageEvent);
            return new SimpleIntegerProperty(noParticipants).asObject();
        });

        tableViewEvents.setItems(modelAgeEvent);

        tableColumnFullName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        tableColumnNoRegistrations.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            int noRegistrations = countRegistrations(participant);
            return new SimpleIntegerProperty(noRegistrations).asObject();
        });

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

            AgeEvent ageEvent = tableViewEvents.getSelectionModel().getSelectedItem();

            SearchController searchController = loader.getController();

            searchController.setService(service, ageEvent);

            dialogStage.show();

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

            Participant currentParticipant = tableViewParticipants.getSelectionModel().getSelectedItem();

            RegisterController registerController = loader.getController();

            registerController.setService(service, currentParticipant, currentEmployee, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int countParticipants(AgeEvent ageEvent) {
        Iterable<Registration> registrations = service.findByAgeEvent(ageEvent);
        Map<Long, Integer> participantsCountMap = new HashMap<>();

        for (Registration registration : registrations) {
            Long eventId = registration.getAgeEvent().getId();
            participantsCountMap.put(eventId, participantsCountMap.getOrDefault(eventId, 0) + 1);
        }

        return participantsCountMap.getOrDefault(ageEvent.getId(), 0);
    }

    private int countRegistrations(Participant participant) {
        Collection<Registration> registrations = service.findByParticipant(participant);
        return registrations.size();
    }
}
