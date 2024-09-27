package com.epam.trainerhours.messaging;

import com.epam.trainerhours.model.DeadLetter;
import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.model.adapters.LocalDateAdapter;
import com.epam.trainerhours.service.TrainingSessionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Receiver {
    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final TrainingSessionService service;
    private final Sender sender;

    public Receiver(TrainingSessionService service, Sender sender) {
        this.service = service;
        this.sender = sender;
    }

    @JmsListener(destination = "add-training")
    public void receiveMessage(String trainingStr) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        TrainingSession training = gson.fromJson(trainingStr, TrainingSession.class);

        if (!isTrainingValid(training)) {
            logger.error("Training session is invalid: " + training);
            sender.sendMessage("dlq", new Gson().toJson(new DeadLetter(trainingStr, "Required fields are missing!")));
            return;
        }

        service.addSession(training);
    }

    @JmsListener(destination = "dlq")
    public void receiveDLQMessage(String deadletter) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        logger.info("Received a dead letter: " + deadletter + "\nReason is: " + gson.fromJson(deadletter, DeadLetter.class).getReason());
    }

    private boolean isTrainingValid(TrainingSession training) {
        return training.getTrDuration() != null
                && training.getIsActive() != null
                && training.getTrDate() != null
                && training.getTrUsername() != null
                && training.getTrFirstname() != null
                && training.getTrLastname() != null;
    }
}

