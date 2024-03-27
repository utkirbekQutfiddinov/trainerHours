package com.epam.trainerhours.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class TrainingSessionResponse {
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean trainerStatus;
    private Map<String, Map<String, Long>> years;
}
