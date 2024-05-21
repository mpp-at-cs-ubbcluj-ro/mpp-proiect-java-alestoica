import config.Config;
import repository.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import server.Service;
import service.*;
import utils.AbstractServer;
import utils.ConcurrentServer;
import utils.ServerException;
import validators.AgeEventValidator;
import validators.EmployeeValidator;
import validators.ParticipantValidator;
import validators.RegistrationValidator;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private final static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties props = new Properties();

        try {
            props.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set.");
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }

        ParticipantValidator participantValidator = new ParticipantValidator();
        EmployeeValidator employeeValidator = new EmployeeValidator();
        AgeEventValidator ageEventValidator = new AgeEventValidator();
        RegistrationValidator registrationValidator = new RegistrationValidator();

        ParticipantRepository participantRepository = new ParticipantDBRepository(participantValidator, props);
//        ParticipantRepository participantRepository = new ParticipantRepositoryHibernate();
        EmployeeDBRepository employeeRepository = new EmployeeDBRepository(employeeValidator, props);
        AgeEventDBRepository ageEventRepository = new AgeEventDBRepository(ageEventValidator, props);
        RegistrationDBRepository registrationRepository = new RegistrationDBRepository(registrationValidator, props, participantRepository, ageEventRepository, employeeRepository);

        ParticipantService participantService = new ParticipantService(participantRepository);
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        AgeEventService ageEventService = new AgeEventService(ageEventRepository);
        RegistrationService registrationService = new RegistrationService(registrationRepository);

//        IService service = getService();
        IService service = new Service(participantService, employeeService, ageEventService, registrationService);

        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch (NumberFormatException nef){
            System.err.println("Wrong Port Number " + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }

        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ConcurrentServer(serverPort, service);
//        AbstractServer server = new ProtoConcurrentServer(serverPort, service);

        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server " + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch(ServerException e){
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }

    static Service getService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        return context.getBean(Service.class);
    }
}
