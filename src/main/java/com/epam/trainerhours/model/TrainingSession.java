package com.epam.trainerhours.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "trainingSessions")
public class TrainingSession {

    private String id;

    private String trUsername;
    private String trFirstname;
    private String trLastname;
    private Boolean isActive;
    private LocalDate trDate;
    private Long trDuration;

}
