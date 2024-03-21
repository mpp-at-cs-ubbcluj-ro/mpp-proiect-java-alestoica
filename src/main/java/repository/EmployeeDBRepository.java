package repository;

import model.Employee;
import model.Participant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validators.EmployeeValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class EmployeeDBRepository implements EmployeeRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private EmployeeValidator validator;

    public EmployeeDBRepository(Properties props) {
        logger.info("initializing EmployeeDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        validator = new EmployeeValidator();
    }

    public Employee findOneByUsernameAndPassword(String username, String password) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("select * from employees where username = ? and password = ?")){

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Long id = result.getLong("id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");

                Employee employee = new Employee(firstName, lastName, username, password);
                employee.setId(id);

                logger.traceExit();
                return employee;
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public Employee findOne(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("select * from employees where id = ?")){

            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String username = result.getString("username");
                String password = result.getString("password");

                Employee employee = new Employee(firstName, lastName, username, password);
                employee.setId(id);

                logger.traceExit();
                return employee;
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public Iterable<Employee> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> employees = new ArrayList<>();

        try(PreparedStatement preparedStatement = con.prepareStatement("select * from employees");
            ResultSet result = preparedStatement.executeQuery();){

            while (result.next()) {
                Long id = result.getLong("id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String username = result.getString("username");
                String password = result.getString("password");

                Employee employee = new Employee(firstName, lastName, username, password);
                employee.setId(id);

                employees.add(employee);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return employees;
    }

    @Override
    public void add(Employee entity) {
        logger.traceEntry("saving employee {}", entity);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("insert into employees (id, first_name, last_name, username, password) values (?, ?, ?, ?, ?)")){

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setString(3, entity.getLastName());
            preparedStatement.setString(4, entity.getUsername());
            preparedStatement.setString(5, entity.getPassword());

            validator.validate(entity);

            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry("deleting employee with id {} ", id);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("delete from employees where id = ?")){

            preparedStatement.setLong(1, id);

            int result = preparedStatement.executeUpdate();
            logger.trace("deleted {} instances", result);

        } catch (SQLException e) {

            logger.error(e);
            System.err.println("db error " + e);

        }

        logger.traceExit();
    }

    @Override
    public void update(Long id, Employee entity) {
        logger.traceEntry("updating employee with id {}", id);
        Connection con = dbUtils.getConnection();

        try(PreparedStatement preparedStatement = con.prepareStatement("update employees set first_name = ?, last_name = ?, username = ?, password = ? where id = ?")){

            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getUsername());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setLong(5, id);

            validator.validate(entity);

            int result = preparedStatement.executeUpdate();
            logger.trace("updated {} instances", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
    }

    @Override
    public Collection<Employee> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Employee> employees = new ArrayList<>();

        try(PreparedStatement preparedStatement = con.prepareStatement("select * from employees");
            ResultSet result = preparedStatement.executeQuery();){

            while (result.next()) {
                Long id = result.getLong("id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String username = result.getString("username");
                String password = result.getString("password");

                Employee employee = new Employee(firstName, lastName, username, password);
                employee.setId(id);

                employees.add(employee);
            }

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("db error " + e);
        }

        logger.traceExit();
        return employees;
    }
}
