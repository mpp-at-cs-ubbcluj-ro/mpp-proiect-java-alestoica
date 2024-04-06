import config.Config;
import controller.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.Service;

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

            logInController.setService(getService());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Service getService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        return context.getBean(Service.class);
    }

    public static void main(String[] args) {
        launch();
    }
}