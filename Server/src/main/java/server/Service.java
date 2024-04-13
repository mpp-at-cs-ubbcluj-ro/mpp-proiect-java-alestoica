package server;

import model.AgeEvent;
import model.Employee;
import model.Participant;
import model.Registration;
import service.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private final ParticipantService participantService;
    private final EmployeeService employeeService;
    private final AgeEventService ageEventService;
    private final RegistrationService registrationService;
    private final Map<Long, IObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    public Service(ParticipantService participantService, EmployeeService employeeService, AgeEventService ageEventService, RegistrationService registrationService) {
        this.participantService = participantService;
        this.employeeService = employeeService;
        this.ageEventService = ageEventService;
        this.registrationService = registrationService;
        loggedClients = new ConcurrentHashMap<>();
    }

    public synchronized Employee login(Employee employee, IObserver client) throws Exception {
        Employee foundEmployee = findOneByUsernameAndPassword(employee.getUsername(), employee.getPassword());

        System.out.println("Service log in");

        if (foundEmployee != null) {
            if (loggedClients.get(foundEmployee.getId()) != null)
                throw new Exception("Employee already logged in!");
            loggedClients.put(foundEmployee.getId(), client);
        }
        else
            throw new Exception("This account doesn't exist!");

        return foundEmployee;
    }

    public synchronized void logout(Long id, IObserver client) throws Exception {
        IObserver localClient = loggedClients.remove(id);

        if (localClient == null)
            throw new Exception("Employee " + id + " is not logged in.");
    }

    public synchronized void notifyAddRegistration(Registration registration) {
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);

        loggedClients.forEach((id, client) -> {
            executorService.execute(() -> {
                try {
                    client.notifyAddRegistration(registration);
                } catch (Exception e) {
                    System.err.println("Error notifying employee with id " + id + " - message: " + e.getMessage());
                }
            });
        });

        executorService.shutdown();
    }

    public synchronized void notifyAddParticipant(Participant participant) {
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);

        loggedClients.forEach((id, client) -> {
            executorService.execute(() -> {
                try {
                    client.notifyAddParticipant(participant);
                } catch (Exception e) {
                    System.err.println("Error notifying employee with id " + id + " - message: " + e.getMessage());
                }
            });
        });

        executorService.shutdown();
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
        notifyAddRegistration(entity);
    }

    public void addParticipant(Participant entity) {
        participantService.add(entity);
        notifyAddParticipant(entity);
    }

    public int countParticipants(AgeEvent ageEvent) {
        return ageEventService.countParticipants(ageEvent.getId());
    }

    public int countRegistrations(Participant participant) {
        return participantService.countRegistrations(participant.getId());
    }
}
