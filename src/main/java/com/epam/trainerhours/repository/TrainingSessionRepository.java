package com.epam.trainerhours.repository;

import com.epam.trainerhours.model.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession,Integer> {
}
