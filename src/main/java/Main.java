import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import repository.*;
import service.AgeEventService;
import service.EmployeeService;
import service.ParticipantService;
import service.RegistrationService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("login-view.fxml"));
            AnchorPane layout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Log In");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(layout));

            LogInController logInController = loader.getController();

            Properties props = new Properties();

            try {
                props.load(new FileReader("bd.config"));
            } catch (IOException e) {
                System.out.println("cannot find bd.config " + e);
            }

            ParticipantRepository participantRepository = new ParticipantDBRepository(props);
            ParticipantService participantService = new ParticipantService(participantRepository);

            EmployeeRepository employeeRepository = new EmployeeDBRepository(props);
            EmployeeService employeeService = new EmployeeService(employeeRepository);

            AgeEventRepository ageEventRepository = new AgeEventDBRepository(props);
            AgeEventService ageEventService = new AgeEventService(ageEventRepository);

            RegistrationRepository registrationRepository = new RegistrationDBRepository(props);
            RegistrationService registrationService = new RegistrationService(registrationRepository);

            logInController.setServices(participantService, employeeService, ageEventService, registrationService);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}