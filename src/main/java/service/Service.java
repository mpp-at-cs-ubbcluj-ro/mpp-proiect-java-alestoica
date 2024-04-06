package service;

import model.AgeEvent;
import model.Employee;
import model.Participant;
import model.Registration;

import java.util.Collection;

public class Service {
    private final ParticipantService participantService;
    private final EmployeeService employeeService;
    private final AgeEventService ageEventService;
    private final RegistrationService registrationService;

    public Service(ParticipantService participantService, EmployeeService employeeService, AgeEventService ageEventService, RegistrationService registrationService) {
        this.participantService = participantService;
        this.employeeService = employeeService;
        this.ageEventService = ageEventService;
        this.registrationService = registrationService;
    }

    public Employee findOneByUsernameAndPassword(String username, String password) {
        return employeeService.findOneByUsernameAndPassword(username, password);
    }

    public Collection<AgeEvent> getAllAgeEvents() {
        return ageEventService.getAll();
    }

    public Collection<Participant> getAllParticipants() {
        return participantService.getAll();
    }

    public Collection<Registration> findByAgeEvent(AgeEvent ageEvent) {
        return registrationService.findByAgeEvent(ageEvent);
    }

    public Collection<Registration> findByParticipant(Participant participant) {
        return registrationService.findByParticipant(participant);
    }

    public Participant findOne(long id) {
        return participantService.findOne(id);
    }

    public Participant findOneByNameAndAge(String firstName, String lastName, int age) {
        return participantService.findOneByNameAndAge(firstName, lastName, age);
    }

    public AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent) {
        return ageEventService.findByAgeGroupAndSportsEvent(ageGroup, sportsEvent);
    }

    public void addRegistration(Registration entity) {
        registrationService.add(entity);
    }

    public void addParticipant(Participant entity) {
        participantService.add(entity);
    }
}
