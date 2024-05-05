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
    private final TrainingSessionRepository<TrainingSession,String> repository;

    public TrainingSessionService(TrainingSessionRepository<TrainingSession,String> repository) {
        this.repository = repository;
    }

    public TrainingSession addSession(TrainingSession session) {
        try {
            List<TrainingSession> list = repository.findByUsername(session.getTrUsername());
            if (!list.isEmpty()) {
                TrainingSession trainingSession = list.getFirst();

                trainingSession.setTrDuration(trainingSession.getTrDuration() + session.getTrDuration());
                Optional<TrainingSession> saved = repository.save(trainingSession);
                return saved.orElseThrow();
            } else {
                Optional<TrainingSession> saved = repository.save(session);
                return saved.orElseThrow();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public boolean deleteSession(String id) {
        try {
            Optional<TrainingSession> byId = repository.findById(id);
            if (byId.isEmpty()) {
                return false;
            }
            repository.deleteById(byId.get().getId());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
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

            return result.entrySet().stream()
                    .map(entry -> new TrainingSessionResponse(
                            entry.getKey(),
                            all.stream().filter(s -> s.getTrUsername().equals(entry.getKey())).findFirst().map(TrainingSession::getTrFirstname).orElse(null),
                            all.stream().filter(s -> s.getTrUsername().equals(entry.getKey())).findFirst().map(TrainingSession::getTrLastname).orElse(null),
                            all.stream().filter(s -> s.getTrUsername().equals(entry.getKey())).findFirst().map(TrainingSession::getIsActive).orElse(null),
                            entry.getValue()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }
}
