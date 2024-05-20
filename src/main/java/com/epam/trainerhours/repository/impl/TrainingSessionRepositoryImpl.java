package com.epam.trainerhours.repository.impl;

import com.epam.trainerhours.model.TrainingSession;
import com.epam.trainerhours.repository.TrainingSessionRepository;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingSessionRepositoryImpl implements TrainingSessionRepository<TrainingSession, String> {
    private final Logger logger = LoggerFactory.getLogger(TrainingSessionRepositoryImpl.class);
    private final MongoTemplate mongoTemplate;

    public TrainingSessionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<TrainingSession> findAll() {
        return mongoTemplate.findAll(TrainingSession.class);
    }

    @Override
    public List<TrainingSession> findByUsername(String username) throws Exception {
        try {
            Query query = new Query(Criteria.where("trUsername").is(username));
            return mongoTemplate.find(query, TrainingSession.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("item not found with username: "+username);
        }
    }

    @Override
    public Optional<TrainingSession> save(TrainingSession item) throws Exception {
        try {
            TrainingSession saved = mongoTemplate.save(item);
            return Optional.of(saved);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("Cannot save item: "+item);
        }
    }

    @Override
    public Optional<TrainingSession> findById(String id) throws Exception {
        try {
            return Optional.ofNullable(mongoTemplate.findById(id, TrainingSession.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("Cannot find by id="+id);
        }
    }

    @Override
    public Optional<Boolean> deleteById(String id) throws Exception {
        try {
            TrainingSession documentToDelete = mongoTemplate.findById(id, TrainingSession.class);

            if (documentToDelete == null) {
                logger.error("Document does not exist with id: " + id);
                return Optional.empty();
            }

            DeleteResult remove = mongoTemplate.remove(documentToDelete);

            if (remove.getDeletedCount() == 0) {
                logger.error("Could not delete session item on ID: " + id);
                return Optional.of(false);
            } else {
                return Optional.of(true);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("Cannot delete item id="+id);
        }
    }

    @Override
    public Optional<Boolean> updateByUsername(String username, TrainingSession document) throws Exception {
        try {
            Query query = new Query(Criteria.where("username").is(username));
            Update update = new Update().set("trDuration", document.getTrDuration());
            UpdateResult result = mongoTemplate.updateFirst(query, update, TrainingSession.class);
            if (result.getModifiedCount() == 0) {
                return Optional.of(false);
            }
            return Optional.of(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("Cannot update document by username="+username);
        }

    }
}
