package service;

import model.AgeEvent;
import model.Participant;
import model.Registration;
import repository.RegistrationRepository;

import java.util.Collection;

public class RegistrationService implements ServiceInterface<Long, Registration> {
    RegistrationRepository repo;

    public RegistrationService(RegistrationRepository repo) {
        this.repo = repo;
    }

    public Collection<Registration> findByAgeEvent(AgeEvent ageEvent) {
        return repo.findByAgeEvent(ageEvent);
    }

    public Collection<Registration> findByParticipant(Participant participant) {
        return repo.findByParticipant(participant);
    }

    @Override
    public Registration findOne(Long id) {
        return repo.findOne(id);
    }

    @Override
    public Iterable<Registration> findAll() {
        return repo.findAll();
    }

    @Override
    public void add(Registration entity) {
        repo.add(entity);
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    public void update(Long id, Registration entity) {
        repo.update(id, entity);
    }

    @Override
    public Collection<Registration> getAll() {
        return repo.getAll();
    }
}
