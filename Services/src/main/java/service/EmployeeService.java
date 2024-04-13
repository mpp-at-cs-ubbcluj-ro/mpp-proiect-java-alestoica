package service;

import model.Employee;
import repository.EmployeeRepository;

import java.util.Collection;

public class EmployeeService implements ServiceInterface<Long, Employee> {
    EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public Employee findOneByUsernameAndPassword(String username, String password) {
        return repo.findOneByUsernameAndPassword(username, password);
    }

    @Override
    public Employee findOne(Long id) {
        return repo.findOne(id);
    }

    @Override
    public Iterable<Employee> findAll() {
        return repo.findAll();
    }

    @Override
    public Collection<Employee> getAll() {
        return repo.getAll();
    }





    @Override
    public void add(Employee entity) {
        repo.add(entity);
    }

    @Override
    public void delete(Long id) {
        repo.delete(id);
    }

    @Override
    public void update(Long id, Employee entity) {
        repo.update(id, entity);
    }
}
