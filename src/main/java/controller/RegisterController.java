package controller;

import controller.MessageAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AgeEvent;
import model.Employee;
import model.Participant;
import model.Registration;
import service.AgeEventService;
import service.ParticipantService;
import service.RegistrationService;
import validators.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegisterController {
    ParticipantService participantService;
    RegistrationService registrationService;
    AgeEventService ageEventService;
    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;
    @FXML
    ComboBox<Integer> comboBoxAge;
    @FXML
    ComboBox<String> comboBoxEvent;
    Participant currentParticipant;
    Employee currentEmployee;
    Stage dialogStage;

    public void setServices(ParticipantService participantService, RegistrationService registrationService, AgeEventService ageEventService, Participant participant, Employee employee, Stage dialogStage) {
        this.participantService = participantService;
        this.registrationService = registrationService;
        this.ageEventService = ageEventService;

        this.currentParticipant = participant;
        this.currentEmployee = employee;

        this.dialogStage = dialogStage;

        List<Integer> ages = new ArrayList<>();
        for (int i = 6; i <= 15; i++) {
            ages.add(i);
        }

        comboBoxAge.getItems().setAll(ages);

        if (currentParticipant != null) {
            textFieldFirstName.setText(currentParticipant.getFirstName());
            textFieldLastName.setText(currentParticipant.getLastName());
            comboBoxAge.getSelectionModel().select(currentParticipant.getAge() - 6);
            comboBoxAge.setDisable(true);

            List<Long> ageEventsIds = new ArrayList<>();
            registrationService.findByParticipant(currentParticipant.getId()).forEach(registration -> ageEventsIds.add(registration.getIdAgeEvent()));

            List<String> ageEventsNames = new ArrayList<>();
            ageEventsIds.forEach(id -> ageEventsNames.add(ageEventService.findOne(id).getSportsEvent().toString()));

            int age = currentParticipant.getAge();

            List<String> events = new ArrayList<>();
            if (age >= 6 && age <= 8) {
                events.add("METERS_50");
                events.add("METERS_100");
            } else if (age >= 9 && age <= 11) {
                events.add("METERS_100");
                events.add("METERS_1000");
            } else if (age >= 12 && age <= 15) {
                events.add("METERS_1000");
                events.add("METERS_1500");
            }

            ageEventsNames.forEach(events::remove);

            comboBoxEvent.getItems().setAll(events);
            comboBoxEvent.getSelectionModel().selectFirst();
        }
        else {
            comboBoxAge.getSelectionModel().selectFirst();
            comboBoxAge.setOnAction(event -> {
                int age = comboBoxAge.getSelectionModel().getSelectedItem();

                List<String> events = new ArrayList<>();
                if (age >= 6 && age <= 8) {
                    events.add("METERS_50");
                    events.add("METERS_100");
                } else if (age >= 9 && age <= 11) {
                    events.add("METERS_100");
                    events.add("METERS_1000");
                } else if (age >= 12 && age <= 15) {
                    events.add("METERS_1000");
                    events.add("METERS_1500");
                }

                comboBoxEvent.getItems().setAll(events);
                comboBoxEvent.getSelectionModel().selectFirst();
            });
            comboBoxAge.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            Long idParticipant = 1111L;

            if (currentParticipant == null) {
                Random random = new Random();
                Long id = random.nextLong(100000);
                String firstName = textFieldFirstName.getText();
                String lastName = textFieldLastName.getText();
                Integer age = comboBoxAge.getSelectionModel().getSelectedItem();

                Participant participant = participantService.findOneByNameAndAge(firstName, lastName, age);

                if (participant == null) {
                    participant = new Participant(firstName, lastName, age);
                    participant.setId(id);

                    idParticipant = id;
                    currentParticipant = participant;

                    participantService.add(participant);
                } else {
                    MessageAlert.showErrorMessage(null, "This participant already exists and you have to select it from the previous page!");
                }
            } else {
                idParticipant = currentParticipant.getId();
                textFieldFirstName.setText(currentParticipant.getFirstName());
                textFieldLastName.setText(currentParticipant.getLastName());
                comboBoxAge.getSelectionModel().select(currentParticipant.getAge());

                List<String> events = new ArrayList<>();
                if (currentParticipant.getAge() >= 6 && currentParticipant.getAge() <= 8) {
                    events.add("METERS_50");
                    events.add("METERS_100");
                } else if (currentParticipant.getAge() >= 9 && currentParticipant.getAge() <= 11) {
                    events.add("METERS_100");
                    events.add("METERS_1000");
                } else if (currentParticipant.getAge() >= 12 && currentParticipant.getAge() <= 15) {
                    events.add("METERS_1000");
                    events.add("METERS_1500");
                }

                comboBoxEvent.getItems().setAll(events);
                comboBoxEvent.getSelectionModel().selectFirst();
            }

            Integer age = comboBoxAge.getSelectionModel().getSelectedItem();

            String sportsEvent = comboBoxEvent.getSelectionModel().getSelectedItem();
            String ageGroup = "GROUP_6_8_YEARS";

            if (age >= 9 && age <= 11) {
                ageGroup = "GROUP_9_11_YEARS";
            } else if (age >= 12 && age <= 15) {
                ageGroup = "GROUP_12_15_YEARS";
            }

            System.out.println(ageGroup);
            System.out.println(sportsEvent);

            AgeEvent ageEvent = ageEventService.findByAgeGroupAndSportsEvent(ageGroup, sportsEvent);

            System.out.println(ageEvent);

            Registration registration = new Registration(idParticipant, ageEvent.getId(), currentEmployee.getId());
            Random random = new Random();
            Long id = random.nextLong(100000);
            registration.setId(id);
            registrationService.add(registration);

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Register", currentParticipant.getFirstName() + " was successfully registered for " + ageEvent.getSportsEvent().toString() + "!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

        dialogStage.close();
    }
}
