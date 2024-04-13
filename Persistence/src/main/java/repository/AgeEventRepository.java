package repository;

import model.AgeEvent;

public interface AgeEventRepository extends Repository<Long, AgeEvent> {
    AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent);
    int countParticipants(Long id);
}
