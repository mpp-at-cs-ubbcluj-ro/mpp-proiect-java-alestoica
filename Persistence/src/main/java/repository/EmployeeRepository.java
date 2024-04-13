package repository;

import model.Employee;

public interface EmployeeRepository extends Repository<Long, Employee> {
    Employee findOneByUsernameAndPassword(String username, String password);
}
