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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void findByUsername_Success() throws Exception {
        String username = "testUser";
        List<TrainingSession> sessions = Collections.singletonList(new TrainingSession());
        Query query = new Query(Criteria.where("trUsername").is(username));
        when(mongoTemplate.find(query, TrainingSession.class)).thenReturn(sessions);

        List<TrainingSession> result = repository.findByUsername(username);

        assertEquals(sessions, result);
    }

    @Test
    void findByUsername_Exception() throws Exception {
        String username = "testUser";
        Query query = new Query(Criteria.where("trUsername").is(username));
        when(mongoTemplate.find(query, TrainingSession.class)).thenThrow(RuntimeException.class);

        Exception exception=assertThrows(Exception.class,()->{
            repository.findByUsername(username);
        });

        assertNotNull(exception);
    }

    @Test
    void save_Success() throws Exception {
        TrainingSession session = new TrainingSession();
        when(mongoTemplate.save(session)).thenReturn(session);

        Optional<TrainingSession> result = repository.save(session);

        assertTrue(result.isPresent());
        assertEquals(session, result.get());
    }

    @Test
    void save_Exception() throws Exception {
        when(mongoTemplate.save(any())).thenThrow(RuntimeException.class);

        Exception exception=assertThrows(Exception.class,()->{
            repository.save(new TrainingSession());
        });

        assertNotNull(exception);
    }

    @Test
    void findById_Success() throws Exception {
        String id = "1";
        TrainingSession session = new TrainingSession();
        when(mongoTemplate.findById(id, TrainingSession.class)).thenReturn(session);

        Optional<TrainingSession> result = repository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(session, result.get());
    }

    @Test
    void findById_NotFound() throws Exception {
        String id = "1";
        when(mongoTemplate.findById(id, TrainingSession.class)).thenReturn(null);

        Optional<TrainingSession> result = repository.findById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_Success() throws Exception {
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
    void deleteById_NotFound() throws Exception {
        String id = "1";
        when(mongoTemplate.findById(id, TrainingSession.class)).thenReturn(null);

        Optional<Boolean> deleted = repository.deleteById(id);

        assertTrue(deleted.isEmpty());

        verify(mongoTemplate, never()).remove(any());
    }

    @Test
    void updateByUsername_Success() throws Exception {
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
    void updateByUsername_NotFound() throws Exception {
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
    void updateByUsername_Exception() throws Exception {
        String username = "testUser";
        TrainingSession session = new TrainingSession();
        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(TrainingSession.class))).thenThrow(new RuntimeException("Test Exception"));

//        Optional<Boolean> result = repository.updateByUsername(username, session);

        Exception exception = assertThrows(Exception.class, () -> {
            repository.updateByUsername(username, session);
        });
        assertNotNull(exception);

    }
}
