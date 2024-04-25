import config.Config;
import repository.ParticipantRepositoryHibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import repository.AgeEventDBRepository;
import repository.EmployeeDBRepository;
import repository.ParticipantRepository;
import repository.RegistrationDBRepository;
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

//        ParticipantDBRepository participantDBRepository = new ParticipantDBRepository(participantValidator, props);
        ParticipantRepository participantRepositoryHibernate = new ParticipantRepositoryHibernate();
        EmployeeDBRepository employeeDBRepository = new EmployeeDBRepository(employeeValidator, props);
        AgeEventDBRepository ageEventDBRepository = new AgeEventDBRepository(ageEventValidator, props);
        RegistrationDBRepository registrationDBRepository = new RegistrationDBRepository(registrationValidator, props, participantRepositoryHibernate, ageEventDBRepository, employeeDBRepository);

        ParticipantService participantService = new ParticipantService(participantRepositoryHibernate);
        EmployeeService employeeService = new EmployeeService(employeeDBRepository);
        AgeEventService ageEventService = new AgeEventService(ageEventDBRepository);
        RegistrationService registrationService = new RegistrationService(registrationDBRepository);

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
