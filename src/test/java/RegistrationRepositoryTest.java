import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.RegistrationDBRepository;
import repository.RegistrationRepository;
import model.Registration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RegistrationRepositoryTest {

    @Mock
    private RegistrationDBRepository repositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByAgeEvent() {
        // Given
        long idAgeEvent = 1L;
        List<Registration> registrations = new ArrayList<>();
        registrations.add(new Registration(1L, idAgeEvent, 3L));
        registrations.add(new Registration(2L, idAgeEvent, 4L));
        when(repositoryMock.findByAgeEvent(idAgeEvent)).thenReturn(registrations);

        // When
        Iterable<Registration> foundRegistrations = repositoryMock.findByAgeEvent(idAgeEvent);

        // Then
        assertEquals(registrations, foundRegistrations);
        verify(repositoryMock, times(1)).findByAgeEvent(idAgeEvent);
    }


    @Test
    void testFindOne() {
        // Given
        long id = 1L;
        Registration registration = new Registration(1L, 2L, 3L);
        registration.setId(id);
        when(repositoryMock.findOne(id)).thenReturn(registration);

        // When
        Registration foundRegistration = repositoryMock.findOne(id);

        // Then
        assertEquals(registration, foundRegistration);
        verify(repositoryMock, times(1)).findOne(id);
    }

    @Test
    void testFindAll() {
        // Given
        List<Registration> registrations = new ArrayList<>();
        registrations.add(new Registration(1L, 2L, 3L));
        registrations.add(new Registration(4L, 5L, 6L));
        when(repositoryMock.findAll()).thenReturn(registrations);

        // When
        Iterable<Registration> foundRegistrations = repositoryMock.findAll();

        // Then
        assertEquals(registrations, foundRegistrations);
        verify(repositoryMock, times(1)).findAll();
    }

    @Test
    void testAdd() {
        // Given
        Registration registration = new Registration(1L, 2L, 3L);
        doNothing().when(repositoryMock).add(registration);

        // When
        repositoryMock.add(registration);

        // Then
        verify(repositoryMock, times(1)).add(registration);
    }

    @Test
    void testUpdate() {
        // Given
        long id = 1L;
        Registration registration = new Registration(1L, 2L, 3L);
        doNothing().when(repositoryMock).update(id, registration);

        // When
        repositoryMock.update(id, registration);

        // Then
        verify(repositoryMock, times(1)).update(id, registration);
    }
}
