package repository;

import model.AgeEvent;
import model.Participant;
import model.Registration;

import java.util.Collection;

public interface RegistrationRepository extends Repository<Long, Registration> {
    Collection<Registration> findByAgeEvent(AgeEvent ageEvent);
    Collection<Registration> findByParticipant(Participant participant);
}
