package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Employee;
import service.IService;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LogInController {
    IService service;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;
    private AccountController accountController;
    Stage currentStage;

    public void setService(IService service, Stage currentStage) {
        this.service = service;
        this.currentStage = currentStage;
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

        Employee employee = new Employee("", "", username, hashedPassword);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/account-view.fxml"));
            AnchorPane layout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("My Account");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(layout));

            accountController = loader.getController();

//            System.out.println(employee);
            System.out.println("Client: (account controller) " + accountController);
            Employee connectedEmployee = service.login(employee, accountController);

            accountController.setService(service, connectedEmployee, dialogStage);
//            System.out.println(connectedEmployee);
//            showAccountDialog(connectedEmployee);

            dialogStage.show();

            currentStage.close();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, "Wrong username or password!\n" + e.getMessage());
        }
    }

    private void showAccountDialog(Employee employee) {
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("../view/account-view.fxml"));
//            AnchorPane layout = loader.load();
//
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("My Account");
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.setScene(new Scene(layout));
//
//            accountController = loader.getController();
//
//            accountController.setService(service, employee, dialogStage);

//            dialogStage.show();
//
//            currentStage.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
