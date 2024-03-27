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

    private String tr_username;
    private String tr_firstname;
    private String tr_lastname;
    private Boolean isActive;
    private LocalDate tr_date;
    private Long tr_duration;
}
