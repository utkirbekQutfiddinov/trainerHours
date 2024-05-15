package com.epam.trainerhours.repository;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.repository.impl.TrainingSessionRepositoryImpl;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingSessionRepositoryImplTest {
    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private TrainingSessionRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAll_Success() {
        List<TrainingSession> sessions = Collections.singletonList(new TrainingSession());
        when(mongoTemplate.findAll(TrainingSession.class)).thenReturn(sessions);

        List<TrainingSession> result = repository.findAll();

        assertEquals(sessions, result);
    }

    @Test
    void findByUsername_Success() {
        String username = "testUser";
        List<TrainingSession> sessions = Collections.singletonList(new TrainingSession());
        Query query = new Query(Criteria.where("trUsername").is(username));
        when(mongoTemplate.find(query, TrainingSession.class)).thenReturn(sessions);

        List<TrainingSession> result = repository.findByUsername(username);

        assertEquals(sessions, result);
    }

    @Test
    void findByUsername_Exception() {
        String username = "testUser";
        Query query = new Query(Criteria.where("trUsername").is(username));
        when(mongoTemplate.find(query, TrainingSession.class)).thenThrow(RuntimeException.class);

        List<TrainingSession> result = repository.findByUsername(username);

        assertEquals(0, result.size());
    }

    @Test
    void save_Success() {
        TrainingSession session = new TrainingSession();
        when(mongoTemplate.save(session)).thenReturn(session);

        Optional<TrainingSession> result = repository.save(session);

        assertTrue(result.isPresent());
        assertEquals(session, result.get());
    }

    @Test
    void save_Exception() {
        when(mongoTemplate.save(any())).thenThrow(RuntimeException.class);

        Optional<TrainingSession> result = repository.save(new TrainingSession());

        assertFalse(result.isPresent());
    }

    @Test
    void findById_Success() {
        String id = "1";
        TrainingSession session = new TrainingSession();
        when(mongoTemplate.findById(id, TrainingSession.class)).thenReturn(session);

        Optional<TrainingSession> result = repository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(session, result.get());
    }

    @Test
    void findById_NotFound() {
        String id = "1";
        when(mongoTemplate.findById(id, TrainingSession.class)).thenReturn(null);

        Optional<TrainingSession> result = repository.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_Success() {
        String id = "1";
        TrainingSession session = new TrainingSession();
        when(mongoTemplate.findById(id, TrainingSession.class)).thenReturn(session);
        DeleteResult deleteResult = mock(DeleteResult.class);
        when(deleteResult.getDeletedCount()).thenReturn(1L);
        when(mongoTemplate.remove(session)).thenReturn(deleteResult);

        Optional<Boolean> deleted = repository.deleteById(id);
        assertTrue(deleted.get());

        verify(mongoTemplate, times(1)).remove(session);
    }

    @Test
    void deleteById_NotFound() {
        String id = "1";
        when(mongoTemplate.findById(id, TrainingSession.class)).thenReturn(null);

        Optional<Boolean> deleted = repository.deleteById(id);

        assertTrue(deleted.isEmpty());

        verify(mongoTemplate, never()).remove(any());
    }

    @Test
    void updateByUsername_Success() {
        String username = "testUser";
        TrainingSession session = new TrainingSession();
        UpdateResult updateResult = mock(UpdateResult.class);
        when(updateResult.getModifiedCount()).thenReturn(1L);
        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(TrainingSession.class))).thenReturn(updateResult);

        Optional<Boolean> result = repository.updateByUsername(username, session);

        assertTrue(result.isPresent());
        assertTrue(result.get());
    }

    @Test
    void updateByUsername_NotFound() {
        String username = "testUser";
        TrainingSession session = new TrainingSession();
        UpdateResult updateResult = mock(UpdateResult.class);
        when(updateResult.getModifiedCount()).thenReturn(0L);
        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(TrainingSession.class))).thenReturn(updateResult);

        Optional<Boolean> result = repository.updateByUsername(username, session);

        assertTrue(result.isPresent());
        assertFalse(result.get());
    }

    @Test
    void updateByUsername_Exception() {
        String username = "testUser";
        TrainingSession session = new TrainingSession();
        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(TrainingSession.class))).thenThrow(new RuntimeException("Test Exception"));

        Optional<Boolean> result = repository.updateByUsername(username, session);

        assertTrue(result.isEmpty());
    }
}
