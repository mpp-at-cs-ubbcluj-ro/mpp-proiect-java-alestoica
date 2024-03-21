package repository;

import model.Participant;

public interface ParticipantRepository extends Repository<Long, Participant> {
    Participant findOneByNameAndAge(String firstName, String lastName, int age);
}
