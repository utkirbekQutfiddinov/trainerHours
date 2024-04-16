package com.epam.trainerhours.messaging;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.model.adapters.LocalDateAdapter;
import com.epam.trainerhours.service.TrainingSessionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Receiver {

    private final TrainingSessionService service;

    public Receiver(TrainingSessionService service) {
        this.service = service;
    }

    @JmsListener(destination = "add-training")
    public void receiveMessage(String training) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        TrainingSession trainingSession = gson.fromJson(training, TrainingSession.class);
        service.addSession(trainingSession);
    }
}

