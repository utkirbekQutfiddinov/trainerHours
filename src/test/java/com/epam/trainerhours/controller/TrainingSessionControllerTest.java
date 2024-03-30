package com.epam.trainerhours.controller;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.model.TrainingSessionResponse;
import com.epam.trainerhours.service.TrainingSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TrainingSessionControllerTest {

    @Mock
    TrainingSessionService service;

    @InjectMocks
    TrainingSessionController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addSession() {
        TrainingSession session = new TrainingSession();
        when(service.addSession(session)).thenReturn(session);

        ResponseEntity<TrainingSession> response = controller.addSession(session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void delete() {
        int id = 1;
        when(service.deleteSesion(id)).thenReturn(true);

        ResponseEntity<Map<String, Boolean>> response = controller.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getSummary() {
        List<TrainingSessionResponse> responseList = Collections.singletonList(new TrainingSessionResponse(null, null, null, null, null));
        when(service.getAllResponse()).thenReturn(responseList);

        ResponseEntity<List<TrainingSessionResponse>> response = controller.getSummary();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseList, response.getBody());
    }
}
