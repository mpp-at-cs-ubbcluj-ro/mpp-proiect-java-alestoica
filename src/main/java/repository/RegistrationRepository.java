package repository;

import model.AgeGroup;
import model.Registration;
import model.SportsEvent;

public interface RegistrationRepository extends Repository<Long, Registration> {
    public Iterable<Registration> findByAgeEvent(Long idAgeEvent);
}
