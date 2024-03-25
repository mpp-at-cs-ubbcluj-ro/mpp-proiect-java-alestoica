import config.AgeEventConfig;
import config.EmployeeConfig;
import config.ParticipantConfig;
import config.RegistrationConfig;
import controller.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.AgeEventService;
import service.EmployeeService;
import service.ParticipantService;
import service.RegistrationService;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/login-view.fxml"));
            AnchorPane layout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Log In");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(layout));

            LogInController logInController = loader.getController();

            logInController.setServices(getParticipantService(), getEmployeeService(), getAgeEventService(), getRegistrationService());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ParticipantService getParticipantService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ParticipantConfig.class);
        return context.getBean(ParticipantService.class);
    }

    static EmployeeService getEmployeeService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(EmployeeConfig.class);
        return context.getBean(EmployeeService.class);
    }

    static AgeEventService getAgeEventService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AgeEventConfig.class);
        return context.getBean(AgeEventService.class);
    }

    static RegistrationService getRegistrationService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RegistrationConfig.class);
        return context.getBean(RegistrationService.class);
    }

    public static void main(String[] args) {
        launch();
    }
}