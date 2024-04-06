package service;

import model.Participant;
import repository.ParticipantRepository;

import java.util.Collection;

public class ParticipantService implements ServiceInterface<Long, Participant> {
    ParticipantRepository repo;

    public ParticipantService(ParticipantRepository repo) {
        this.repo = repo;
    }

    public Participant findOneByNameAndAge(String firstName, String lastName, int age) {
        return repo.findOneByNameAndAge(firstName, lastName, age);
    }

    @Override
    public Participant findOne(Long id) {
        return repo.findOne(id);
    }

    @Override
    public Iterable<Participant> findAll() {
        return repo.findAll();
    }

    @Override
    public void add(Participant entity) {
        repo.add(entity);
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    public void update(Long id, Participant entity) {
        repo.update(id, entity);
    }

    @Override
    public Collection<Participant> getAll() {
        return repo.getAll();
    }
}
