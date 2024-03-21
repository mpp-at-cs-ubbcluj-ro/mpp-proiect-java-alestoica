package repository;

import model.Registration;

import java.util.Collection;

public interface RegistrationRepository extends Repository<Long, Registration> {
    Collection<Registration> findByAgeEvent(Long idAgeEvent);
    Collection<Registration> findByParticipant(Long idParticipant);
}
