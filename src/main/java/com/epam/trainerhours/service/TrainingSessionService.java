package com.epam.trainerhours.service;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.model.TrainingSessionResponse;
import com.epam.trainerhours.repository.TrainingSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingSessionService {
    private final Logger logger = LoggerFactory.getLogger(TrainingSessionService.class);
    private final TrainingSessionRepository repository;

    public TrainingSessionService(TrainingSessionRepository repository) {
        this.repository = repository;
    }

    public TrainingSession addSession(TrainingSession session) {
        try {
            TrainingSession saved = repository.save(session);
            return saved;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    public boolean deleteSesion(Integer id) {
        try {
            Optional<TrainingSession> byId = repository.findById(id);
            if (byId.isEmpty()) {
                return false;
            }
            repository.delete(byId.get());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException();
        }
    }


    public List<TrainingSessionResponse> getAllResponse() {
        try {


            List<TrainingSession> all = repository.findAll();

            Map<String, Map<String, Map<String, Long>>> result = all.stream()
                    .collect(Collectors.groupingBy(
                            TrainingSession::getTrUsername,
                            Collectors.groupingBy(
                                    session -> String.valueOf(session.getTrDate().getYear()),
                                    Collectors.groupingBy(
                                            session -> session.getTrDate().getMonth().toString(),
                                            Collectors.summingLong(session -> session.getTrDuration().intValue())
                                    )
                            )
                    ));

            List<TrainingSessionResponse> responseList = result.entrySet().stream()
                    .map(entry -> new TrainingSessionResponse(
                            entry.getKey(),
                            all.stream().filter(s -> s.getTrUsername().equals(entry.getKey())).findFirst().map(TrainingSession::getTrFirstname).orElse(null),
                            all.stream().filter(s -> s.getTrUsername().equals(entry.getKey())).findFirst().map(TrainingSession::getTrLastname).orElse(null),
                            all.stream().filter(s -> s.getTrUsername().equals(entry.getKey())).findFirst().map(TrainingSession::getIsActive).orElse(null),
                            entry.getValue()))
                    .collect(Collectors.toList());
            return responseList;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException();
        }
    }
}
