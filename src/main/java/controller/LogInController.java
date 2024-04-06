package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Employee;
import service.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LogInController {
    Service service;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;

    public void setService(Service service) {
        this.service = service;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = digest.digest(password.getBytes());

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void handleLogIn() {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        String hashedPassword = hashPassword(password);

        Employee employee = service.findOneByUsernameAndPassword(username, hashedPassword);

        if (employee != null)
            showAccountDialog(employee);
        else
            MessageAlert.showErrorMessage(null, "Wrong username or password!");
    }

    private void showAccountDialog(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/account-view.fxml"));
            AnchorPane layout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("My Account");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(layout));

            AccountController accountController = loader.getController();

            accountController.setService(service, employee);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
