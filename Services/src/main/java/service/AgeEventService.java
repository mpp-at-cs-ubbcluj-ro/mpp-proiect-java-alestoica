package service;

import model.AgeEvent;
import repository.AgeEventRepository;

import java.util.Collection;

public class AgeEventService implements ServiceInterface<Long, AgeEvent> {
    AgeEventRepository repo;

    public AgeEventService(AgeEventRepository repo) {
        this.repo = repo;
    }

    public int countParticipants(Long id) {
        return repo.countParticipants(id);
    }

    public AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent) {
        return repo.findByAgeGroupAndSportsEvent(ageGroup, sportsEvent);
    }

    @Override
    public AgeEvent findOne(Long id) {
        return repo.findOne(id);
    }

    @Override
    public Iterable<AgeEvent> findAll() {
        return repo.findAll();
    }

    @Override
    public void add(AgeEvent entity) {
        repo.add(entity);
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    public void update(Long id, AgeEvent entity) {
        repo.update(id, entity);
    }

    @Override
    public Collection<AgeEvent> getAll() {
        return repo.getAll();
    }
}
