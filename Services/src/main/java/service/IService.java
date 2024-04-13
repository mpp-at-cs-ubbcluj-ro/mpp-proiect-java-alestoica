package service;

import model.AgeEvent;
import model.Employee;
import model.Participant;
import model.Registration;

import java.util.Collection;

public interface IService {
    Employee login(Employee employee, IObserver client) throws Exception;
    void logout(Long id, IObserver client) throws Exception;
    Employee findOneByUsernameAndPassword(String username, String password);

    Collection<AgeEvent> getAllAgeEvents();

    Collection<Participant> getAllParticipants();

    Collection<Registration> findByAgeEvent(AgeEvent ageEvent);

    Collection<Registration> findByParticipant(Participant participant);

    Participant findOne(long id);

    Participant findOneByNameAndAge(String firstName, String lastName, int age);

    AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent);

    void addRegistration(Registration entity);

    void addParticipant(Participant entity);

    int countParticipants(AgeEvent ageEvent);

    int countRegistrations(Participant participant);
}
