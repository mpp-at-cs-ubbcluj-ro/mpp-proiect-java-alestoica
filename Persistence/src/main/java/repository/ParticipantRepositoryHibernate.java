package repository;

import model.Participant;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Collection;
import java.util.Objects;

public class ParticipantRepositoryHibernate implements ParticipantRepository {
    @Override
    public Participant findOneByNameAndAge(String firstName, String lastName, int age) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Participant where firstName=?1 and lastName=?2 and age=?3", Participant.class)
                    .setParameter(1, firstName)
                    .setParameter(2, lastName)
                    .setParameter(3, age)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public int countRegistrations(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                """
                select count(*) from hibernate.Registration
                where participant.id = ?1
                """, Long.class);

            query.setParameter(1, id);
            Long count = query.uniqueResult();

            return count != null ? count.intValue() : 0;
        }
    }

    @Override
    public Participant findOne(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Participant where id=?1", Participant.class)
                    .setParameter(1, id)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public Iterable<Participant> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Participant", Participant.class).getResultList();
        }
    }

    @Override
    public void add(Participant participant) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(participant));
    }

    @Override
    public void delete(Long id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Participant participant = session.createQuery("from Participant where id=?1", Participant.class)
                    .setParameter(1,id).uniqueResult();

            if (participant != null) {
                session.remove(participant);
                session.flush();
            }
        });
    }

    @Override
    public void update(Long id, Participant participant) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (!Objects.isNull(session.find(Participant.class, id))) {
                session.merge(participant);
                session.flush();
            }
        });
    }

    @Override
    public Collection<Participant> getAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Participant", Participant.class).getResultList();
        }
    }
}
