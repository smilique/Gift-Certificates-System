package com.epam.esm.repositories;

import com.epam.esm.entities.Entity;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents a pattern for interacting with database
 *
 * @author Anton Tomashevich
 * @version 1.0
 * @see com.epam.esm.repository
 * @param <T>
 * @see com.epam.esm.entities.Entity
 */
public interface CrudRepositoryInterface<T extends Entity> {

    /**
     * The method for getting from multiple entities from db
     *
     * @return List<T> list of entities
     */
    List<T> findAll();

    /**
     * The method for getting single instance of entity from db
     *
     * @param id identifier of entity
     * @return Optional<T>
     */
    Optional<T> findById(Long id);

    /**
     * The method for adding single entity data in db
     *
     * @param entity Entity to insert into db {@link Entity}
     */
    void save(T entity);

    /**
     * The method for removing single entry in db
     *
     * @param id unique identifier of entity
     */
    void delete(Long id);

    /**
     * The method for updating single entity fields in db
     *
     * @param entity Entity with updated fields {@link Entity}
     */
    void update(T entity);

}
