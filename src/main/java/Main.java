import model.*;
import repository.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties props=new Properties();

        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("cannot find bd.config " + e);
        }

        ParticipantRepository participantRepository = new ParticipantDBRepository(props);

        System.out.println("Participants: ");
        for(Participant participant : participantRepository.findAll())
            System.out.println(participant);

        System.out.println();

        AgeEventRepository ageEventRepository = new AgeEventDBRepository(props);

        System.out.println("Age events: ");
        for(AgeEvent ageEvent : ageEventRepository.findAll())
            System.out.println(ageEvent);

        System.out.println();

        EmployeeRepository employeeRepository = new EmployeeDBRepository(props);

        System.out.println("Employees: ");
        for(Employee employee : employeeRepository.findAll())
            System.out.println(employee);

        System.out.println();

        RegistrationRepository registrationRepository = new RegistrationDBRepository(props);

        System.out.println("Registrations: ");
        for(Registration registration : registrationRepository.findAll())
            System.out.println(registration);

        System.out.println();
    }
}