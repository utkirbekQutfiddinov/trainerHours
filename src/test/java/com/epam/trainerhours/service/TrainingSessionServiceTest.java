package com.epam.trainerhours.service;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.model.TrainingSessionResponse;
import com.epam.trainerhours.repository.TrainingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingSessionServiceTest {
    @Mock
    private TrainingSessionRepository<TrainingSession,String> repository;

    private TrainingSessionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TrainingSessionService(repository);
    }

    @Test
    void addSession_NewSession() {
        TrainingSession session = new TrainingSession();
        when(repository.findByUsername(session.getTrUsername())).thenReturn(Collections.emptyList());
        when(repository.save(session)).thenReturn(Optional.of(session));

        TrainingSession result = service.addSession(session);

        assertEquals(session, result);
        verify(repository, times(1)).save(session);
    }

//    @Test
//    void addSession_ExistingSession() {
//        TrainingSession session = new TrainingSession();
//        List<TrainingSession> existingSessions = Collections.singletonList(new TrainingSession());
//        when(repository.findByUsername(session.getTrUsername())).thenReturn(existingSessions);
//        when(repository.save(any(TrainingSession.class))).thenReturn(Optional.of(session));
//
//        TrainingSession result = service.addSession(session);
//
//        assertEquals(session, result);
//        verify(repository, times(1)).save(any(TrainingSession.class));
//    }

    @Test
    void deleteSession_Success() {
        String id = "1";
        TrainingSession session = new TrainingSession();
        session.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(session));

        boolean result = service.deleteSession(id);

        assertTrue(result);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deleteSession_Failure() {
        String id = "1";
        when(repository.findById(id)).thenReturn(Optional.empty());

        boolean result = service.deleteSession(id);

        assertFalse(result);
        verify(repository, never()).deleteById(id);
    }

    @Test
    void deleteSession_Exception() {
        String id = "1";
        when(repository.findById(id)).thenThrow(RuntimeException.class);

        boolean result = service.deleteSession(id);

        assertFalse(result);
        verify(repository, never()).deleteById(id);
    }

    @Test
    void getAllResponse_Success() {
        List<TrainingSession> sessions = Arrays.asList(
                new TrainingSession("1", "user1", "Utkirbek", "TEST", true, LocalDate.now(), 60l),
                new TrainingSession("2", "user1", "Utkirbek", "TEST", true, LocalDate.now().minusDays(1), 30l),
                new TrainingSession("3", "user2", "TestUser", "TEST", true, LocalDate.now().minusMonths(1), 45l)
        );
        when(repository.findAll()).thenReturn(sessions);

        List<TrainingSessionResponse> response = service.getAllResponse();

        assertEquals(2, response.size());
        assertEquals("user1", response.getFirst().getTrainerUsername());
        assertEquals("Utkirbek", response.getFirst().getTrainerFirstname());
        assertEquals("TEST", response.getFirst().getTrainerLastname());
    }

    @Test
    void getAllResponse_Empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<TrainingSessionResponse> response = service.getAllResponse();

        assertEquals(0, response.size());
    }
}
