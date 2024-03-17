import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.EmployeeDBRepository;
import repository.EmployeeRepository;
import model.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeRepositoryTest {

    @Mock
    private EmployeeDBRepository repositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindOne() {
        // Given
        long id = 1L;
        Employee employee = new Employee("John", "Doe", "johndoe", "password");
        employee.setId(id);
        when(repositoryMock.findOne(id)).thenReturn(employee);

        // When
        Employee foundEmployee = repositoryMock.findOne(id);

        // Then
        assertEquals(employee, foundEmployee);
        verify(repositoryMock, times(1)).findOne(id);
    }

    @Test
    void testFindAll() {
        // Given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John", "Doe", "johndoe", "password"));
        employees.add(new Employee("Jane", "Smith", "janesmith", "password"));
        when(repositoryMock.findAll()).thenReturn(employees);

        // When
        Iterable<Employee> foundEmployees = repositoryMock.findAll();

        // Then
        assertEquals(employees, foundEmployees);
        verify(repositoryMock, times(1)).findAll();
    }

    @Test
    void testAdd() throws SQLException {
        // Given
        Employee employee = new Employee("John", "Doe", "johndoe", "password");
        doNothing().when(repositoryMock).add(employee);

        // When
        repositoryMock.add(employee);

        // Then
        verify(repositoryMock, times(1)).add(employee);
    }

    @Test
    void testUpdate() {
        // Given
        long id = 1L;
        Employee employee = new Employee("John", "Doe", "johndoe", "password");
        doNothing().when(repositoryMock).update(id, employee);

        // When
        repositoryMock.update(id, employee);

        // Then
        verify(repositoryMock, times(1)).update(id, employee);
    }
}
