package repository;

import model.AgeEvent;
import model.Registration;

public interface AgeEventRepository extends Repository<Long, AgeEvent> {
    AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent);
}
