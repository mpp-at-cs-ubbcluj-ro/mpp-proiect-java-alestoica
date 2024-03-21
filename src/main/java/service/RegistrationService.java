package service;

import model.Registration;
import repository.RegistrationRepository;

import java.util.Collection;

public class RegistrationService implements Service<Long, Registration> {
    RegistrationRepository repo;

    public RegistrationService(RegistrationRepository repo) {
        this.repo = repo;
    }

    public Collection<Registration> findByAgeEvent(Long idAgeEvent) {
        return repo.findByAgeEvent(idAgeEvent);
    }

    public Collection<Registration> findByParticipant(Long idParticipant) {
        return repo.findByParticipant(idParticipant);
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
