import controller.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import protobufprotocol.ProtoProxy;
import rpcprotocol.ClientWorker;
import rpcprotocol.Proxy;
import service.IService;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    private static int defaultPort = 55556;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage stage) throws IOException {
        Properties props = new Properties();

        try {
            props.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set.");
        } catch (IOException e) {
            System.out.println("Cannot find client.properties " + e);
            return;
        }

        String serverIP = props.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number: " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/login-view.fxml"));
            AnchorPane layout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Log In");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(layout));

            LogInController logInController = loader.getController();

//            IService service = new Proxy(serverIP, serverPort);
            IService service = new ProtoProxy(serverIP, serverPort);

            logInController.setService(service, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
