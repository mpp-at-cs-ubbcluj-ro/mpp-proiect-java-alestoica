package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AgeEvent;
import model.Participant;
import model.Registration;
import service.IService;

import java.util.ArrayList;
import java.util.Collection;

public class SearchController {
    IService service;
    @FXML
    Label titleLabel1;
    @FXML
    Label titleLabel2;
    @FXML
    Label titleLabel3;
    @FXML
    Label titleLabel4;
    ObservableList<Participant> model = FXCollections.observableArrayList();
    @FXML
    TableView<Participant> tableView;
    @FXML
    TableColumn<Participant, String> tableColumnFullName;
    @FXML
    TableColumn<Participant, Integer> tableColumnAge;
    @FXML
    TableColumn<Participant, Integer> tableColumnNoRegistrations;
    AgeEvent currentAgeEvent;

    public void setService(IService service, AgeEvent ageEvent) {
        this.service = service;

        this.currentAgeEvent = ageEvent;

        String minAge = currentAgeEvent.getAgeGroup().toString().split("_")[1];
        String maxAge = currentAgeEvent.getAgeGroup().toString().split("_")[2];

        String meters = currentAgeEvent.getSportsEvent().toString().split("_")[1];

        this.titleLabel1.setText("Participants registered for");
        this.titleLabel2.setText("age group " + minAge + " - " + maxAge);
        this.titleLabel3.setText("and for");
        this.titleLabel4.setText(meters + " meters:");

        initModel();
    }

    private void initModel() {
        Collection<Registration> registrations = service.findByAgeEvent(currentAgeEvent);
        Collection<Participant> participants = new ArrayList<>();

        registrations.forEach(registration -> {
            Participant participant = service.findOne(registration.getParticipant().getId());
            participants.add(participant);
        });

        model.setAll(participants);
    }

    @FXML
    private void initialize() {
        tableColumnFullName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        tableColumnNoRegistrations.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            int noRegistrations = countRegistrations(participant);
            return new SimpleIntegerProperty(noRegistrations).asObject();
        });

        tableView.setItems(model);
    }

    private int countRegistrations(Participant participant) {
        Collection<Registration> registrations = service.findByParticipant(participant);
        return registrations.size();
    }
}
