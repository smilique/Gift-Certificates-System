package com.epam.esm.services;

import com.epam.esm.entities.Entity;

import java.util.List;
import java.util.Optional;

/**
 * The interface declares its successors must have methods for CRUD operations
 *
 * @author Anton Tamashevich
 * @version 1.0
 * @param <T>
 * @see com.epam.esm.entities.Entity
 */
public interface CrudService<T extends Entity> {

    /**
     * Get all entities of T
     *
     * @return List<T>
     */
    List<T> getAll();

    /**
     * Save T entity into the db
     *
     * @param t object of T extends Entity
     */
    void save(T t);

    /**
     * Get single entity of T  with requested id
     *
     * @return Optional<T>
     */
    Optional<T> get(Long id);

    /**
     * Save changes in T entity
     *
     * @param t object of T extends Entity
     */
    void update(T t);

    /**
     * Delete T entity from db with requested id
     *
     * @param id unique id of T extends Entity
     */
    void delete(Long id);

}
