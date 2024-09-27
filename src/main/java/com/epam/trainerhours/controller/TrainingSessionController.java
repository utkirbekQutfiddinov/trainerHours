package com.epam.trainerhours.controller;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.model.TrainingSessionResponse;
import com.epam.trainerhours.service.TrainingSessionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/training-session")
public class TrainingSessionController {
    private final Logger logger = LoggerFactory.getLogger(TrainingSessionController.class);
    private final TrainingSessionService service;

    public TrainingSessionController(TrainingSessionService service) {
        this.service = service;
    }

    @PostMapping
    @CircuitBreaker(name = "addSession", fallbackMethod = "addSessionFallback")
    public ResponseEntity<TrainingSession> addSession(@RequestBody TrainingSession session) {
        try {
            TrainingSession trainingSession = service.addSession(session);

            if (trainingSession != null) {
                return new ResponseEntity<>(trainingSession, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "deleteSession", fallbackMethod = "deleteSessionFallback")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable Integer id) {
        try {
            boolean deleted = service.deleteSesion(id);
            return new ResponseEntity<>(new HashMap<>() {{
                put("success", deleted);
            }}, deleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @CircuitBreaker(name = "getSessions", fallbackMethod = "getSummaryFallback")
    public ResponseEntity<List<TrainingSessionResponse>> getSummary() {
        try {
            List<TrainingSessionResponse> all = service.getAllResponse();
            return new ResponseEntity<>(all, HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, String>> addSessionFallback(Throwable throwable) {
        return new ResponseEntity<>(Map.of("error", throwable.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Map<String, String>> deleteSessionFallback(Throwable throwable) {
        return new ResponseEntity<>(Map.of("error", throwable.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Map<String, String>> getSummaryFallback(Throwable throwable) {
        return new ResponseEntity<>(Map.of("error", throwable.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
