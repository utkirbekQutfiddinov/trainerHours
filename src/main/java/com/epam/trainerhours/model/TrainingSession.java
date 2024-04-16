package com.epam.trainerhours.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String trUsername;
    private String trFirstname;
    private String trLastname;
    private Boolean isActive;
    private LocalDate trDate;
    private Long trDuration;
}
