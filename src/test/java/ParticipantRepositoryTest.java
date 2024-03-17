import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.ParticipantDBRepository;
import repository.ParticipantRepository;
import model.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ParticipantRepositoryTest {

    @Mock
    private ParticipantDBRepository repositoryMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindOne() {
        // Given
        long id = 1L;
        Participant participant = new Participant("Rachel", "Zane", 13);
        participant.setId(id);
        when(repositoryMock.findOne(id)).thenReturn(participant);

        // When
        Participant foundParticipant = repositoryMock.findOne(id);

        // Then
        assertEquals(participant, foundParticipant);
        verify(repositoryMock, times(1)).findOne(id);
    }

    @Test
    void testFindAll() {
        // Given
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("Rachel", "Zane", 13));
        participants.add(new Participant("Mike", "Ross", 15));
        when(repositoryMock.findAll()).thenReturn(participants);

        // When
        Iterable<Participant> foundParticipants = repositoryMock.findAll();

        // Then
        assertEquals(participants, foundParticipants);
        verify(repositoryMock, times(1)).findAll();
    }

    @Test
    void testAdd() {
        // Given
        Participant participant = new Participant("Rachel", "Zane", 13);
        doNothing().when(repositoryMock).add(participant);

        // When
        repositoryMock.add(participant);

        // Then
        verify(repositoryMock, times(1)).add(participant);
    }

    @Test
    void testUpdate() {
        // Given
        long id = 1L;
        Participant participant = new Participant("Rachel", "Zane", 13);
        doNothing().when(repositoryMock).update(id, participant);

        // When
        repositoryMock.update(id, participant);

        // Then
        verify(repositoryMock, times(1)).update(id, participant);
    }
}
