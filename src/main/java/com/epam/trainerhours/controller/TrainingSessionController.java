package com.epam.trainerhours.controller;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.model.TrainingSessionResponse;
import com.epam.trainerhours.service.TrainingSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/training-session")
public class TrainingSessionController {
    private final Logger logger = Logger.getLogger(TrainingSessionController.class.getName());
    private final TrainingSessionService service;

    public TrainingSessionController(TrainingSessionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TrainingSession> addSession(@RequestBody TrainingSession session) {
        try {
            TrainingSession trainingSession = service.addSession(session);

            if (trainingSession != null) {
                return new ResponseEntity<>(trainingSession, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable Integer id) {
        try {
            boolean deleted = service.deleteSesion(id);
            return new ResponseEntity<>(new HashMap<>() {{
                put("success", deleted);
            }}, deleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<TrainingSessionResponse>> getSummary() {
        try {
            List<TrainingSessionResponse> all = service.getAllResponse();
            return new ResponseEntity<>(all, HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
