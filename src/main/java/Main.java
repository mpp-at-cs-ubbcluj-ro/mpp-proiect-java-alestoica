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

        System.out.println("findOne(): ");
        System.out.println(participantRepository.findOne(1234L));

        System.out.println("findAll(): ");
        for(Participant participant : participantRepository.findAll())
            System.out.println(participant);

        System.out.println("add(): ");
        Participant participantAdd = new Participant("Rachel", "Zane", 11);
        participantAdd.setId(1111L);
        participantRepository.add(participantAdd);
        for(Participant participant : participantRepository.findAll())
            System.out.println(participant);

        participantRepository.delete(1111L);

        System.out.println();

        AgeEventRepository ageEventRepository = new AgeEventDBRepository(props);

        System.out.println("findByAgeGroupAndSportsEvent(): ");
        for(AgeEvent ageEvent : ageEventRepository.findByAgeGroupAndSportsEvent("GROUP_6_8_YEARS", "METERS_50"))
            System.out.println(ageEvent);

        System.out.println("findOne(): ");
        System.out.println(ageEventRepository.findOne(1L));

        System.out.println("findAll(): ");
        for(AgeEvent ageEvent : ageEventRepository.findAll())
            System.out.println(ageEvent);

        System.out.println();

        EmployeeRepository employeeRepository = new EmployeeDBRepository(props);

        System.out.println("findOne(): ");
        System.out.println(employeeRepository.findOne(1234L));

        System.out.println("findAll(): ");
        for(Employee employee : employeeRepository.findAll())
            System.out.println(employee);

        System.out.println("add(): ");
        Employee employeeAdd = new Employee("Rachel", "Zane", "rachel_zane", "password");
        employeeAdd.setId(1111L);
        employeeRepository.add(employeeAdd);
        for(Employee employee : employeeRepository.findAll())
            System.out.println(employee);

        employeeRepository.delete(1111L);

        System.out.println();

        RegistrationRepository registrationRepository = new RegistrationDBRepository(props);

        System.out.println("findByAgeEvent():");
        for(Registration registration : registrationRepository.findByAgeEvent(1L))
            System.out.println(registration);

        System.out.println("findOne(): ");
        System.out.println(registrationRepository.findOne(1001L));

        System.out.println("findAll(): ");
        for(Registration registration : registrationRepository.findAll())
            System.out.println(registration);

        System.out.println("add(): ");
        Registration registrationAdd = new Registration(56789L, 2L, 5432L);
        registrationAdd.setId(1111L);
        registrationRepository.add(registrationAdd);
        for(Registration registration : registrationRepository.findAll())
            System.out.println(registration);

        registrationRepository.delete(1111L);

        System.out.println();
    }
}