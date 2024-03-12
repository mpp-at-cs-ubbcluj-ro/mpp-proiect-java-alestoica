package repository;

import model.Entity;

import java.util.Collection;

public interface Repository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    void add(E entity);
    void delete(ID id);
    void update(ID id, E entity);
    Collection<E> getAll();
}
