package com.epam.trainerhours.repository;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionRepository<T, ID> {
    List<T> findAll() throws Exception;

    List<T> findByUsername(String username) throws Exception;

    Optional<T> save(T item) throws Exception;

    Optional<T> findById(ID id) throws Exception;

    Optional<Boolean> deleteById(ID id) throws Exception;

    Optional<Boolean> updateByUsername(String username, T document) throws Exception;
}
