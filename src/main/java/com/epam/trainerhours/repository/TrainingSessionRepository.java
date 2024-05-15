package com.epam.trainerhours.repository;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionRepository<T, ID> {
    List<T> findAll();

    List<T> findByUsername(String username);

    Optional<T> save(T item);

    Optional<T> findById(ID id);

    Optional<Boolean> deleteById(ID id);

    Optional<Boolean> updateByUsername(String username, T document);
}
