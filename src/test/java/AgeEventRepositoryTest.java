import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.AgeEventDBRepository;
import repository.AgeEventRepository;
import model.AgeEvent;
import model.AgeGroup;
import model.SportsEvent;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AgeEventRepositoryTest {

    @Mock
    private AgeEventDBRepository repositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByAgeGroupAndSportsEvent() {
        // Given
        String ageGroup = "GROUP_6_8_YEARS";
        String sportsEvent = "METERS_50";
        List<AgeEvent> ageEvents = new ArrayList<>();
        ageEvents.add(new AgeEvent(AgeGroup.GROUP_6_8_YEARS, SportsEvent.METERS_50));
        ageEvents.add(new AgeEvent(AgeGroup.GROUP_6_8_YEARS, SportsEvent.METERS_100));
        when(repositoryMock.findByAgeGroupAndSportsEvent(ageGroup, sportsEvent)).thenReturn(ageEvents);

        // When
        Iterable<AgeEvent> foundAgeEvents = repositoryMock.findByAgeGroupAndSportsEvent(ageGroup, sportsEvent);

        // Then
        assertEquals(ageEvents, foundAgeEvents);
        verify(repositoryMock, times(1)).findByAgeGroupAndSportsEvent(ageGroup, sportsEvent);
    }


    @Test
    void testFindOne() {
        // Given
        long id = 1L;
        AgeEvent ageEvent = new AgeEvent(AgeGroup.GROUP_12_15_YEARS, SportsEvent.METERS_1500);
        ageEvent.setId(id);
        when(repositoryMock.findOne(id)).thenReturn(ageEvent);

        // When
        AgeEvent foundAgeEvent = repositoryMock.findOne(id);

        // Then
        assertEquals(ageEvent, foundAgeEvent);
        verify(repositoryMock, times(1)).findOne(id);
    }

    @Test
    void testFindAll() {
        // Given
        List<AgeEvent> ageEvents = new ArrayList<>();
        ageEvents.add(new AgeEvent(AgeGroup.GROUP_6_8_YEARS, SportsEvent.METERS_50));
        ageEvents.add(new AgeEvent(AgeGroup.GROUP_6_8_YEARS, SportsEvent.METERS_100));
        when(repositoryMock.findAll()).thenReturn(ageEvents);

        // When
        Iterable<AgeEvent> foundAgeEvents = repositoryMock.findAll();

        // Then
        assertEquals(ageEvents, foundAgeEvents);
        verify(repositoryMock, times(1)).findAll();
    }
}
