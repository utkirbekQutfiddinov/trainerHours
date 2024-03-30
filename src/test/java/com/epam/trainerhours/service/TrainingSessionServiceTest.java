package com.epam.trainerhours.service;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.model.TrainingSessionResponse;
import com.epam.trainerhours.repository.TrainingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingSessionServiceTest {
    @Mock
    private TrainingSessionRepository repository;

    private TrainingSessionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TrainingSessionService(repository);
    }

    @Test
    void addSession_Success() {
        TrainingSession session = new TrainingSession();
        when(repository.save(session)).thenReturn(session);

        TrainingSession savedSession = service.addSession(session);

        assertNotNull(savedSession);
        verify(repository, times(1)).save(session);
    }

    @Test
    void addSession_Failure() {
        TrainingSession session = new TrainingSession();
        when(repository.save(session)).thenThrow(new RuntimeException("Database connection failed"));

        assertThrows(RuntimeException.class, () -> service.addSession(session));
        verify(repository, times(1)).save(session);
    }

    @Test
    void deleteSession_Exists() {
        TrainingSession session = new TrainingSession();
        when(repository.findById(1)).thenReturn(Optional.of(session));

        assertTrue(service.deleteSesion(1));
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(session);
    }

    @Test
    void deleteSession_NotExists() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertFalse(service.deleteSesion(1));
        verify(repository, times(1)).findById(1);
        verify(repository, never()).delete(any());
    }

    @Test
    void deleteSession_Failure() {
        when(repository.findById(1)).thenReturn(Optional.of(new TrainingSession()));
        doThrow(new RuntimeException("Database connection failed")).when(repository).delete(any());

        assertThrows(RuntimeException.class, () -> service.deleteSesion(1));
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).delete(any());
    }

    @Test
    void getAllResponse_Success() {
        List<TrainingSession> sessions = new ArrayList<>();
        when(repository.findAll()).thenReturn(sessions);

        List<TrainingSessionResponse> responses = service.getAllResponse();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllResponse_Failure() {
        when(repository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        assertThrows(RuntimeException.class, () -> service.getAllResponse());
        verify(repository, times(1)).findAll();
    }
}
