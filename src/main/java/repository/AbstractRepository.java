package repository;

import model.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AbstractRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected Map<ID, E> entities;

    public AbstractRepository() {
        this.entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id) {
        if(entities.containsKey(id))
            return entities.get(id);
        else
            throw new RuntimeException("This entity doesn't exist!");
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public void add(E entity) {
        if(entities.containsKey(entity.getId()))
            throw new RuntimeException("This entity already exists!");
        else
            entities.put(entity.getId(), entity);
    }

    @Override
    public void delete(ID id) {
        if(entities.containsKey(id))
            entities.remove(id);
        else
            throw new RuntimeException("This entity doesn't exist!");
    }

    @Override
    public void update(ID id, E entity) {
        if(entities.containsKey(id))
            entities.put(id, entity);
        else
            throw new RuntimeException("This entity doesn't exist!");
    }

    @Override
    public Collection<E> getAll() {
        return entities.values();
    }
}
