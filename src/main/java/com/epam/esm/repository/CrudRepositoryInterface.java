package com.epam.esm.repository;

import com.epam.esm.entity.Entity;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents a pattern for interacting with database
 *
 * @author Anton Tomashevich
 * @see com.epam.esm.repository
 * @param <T>
 */

public interface CrudRepositoryInterface<T extends Entity> {

    /**
     * The method for getting from multiple entities from db
     *
     * @return List<T> list of entities
     * @throws RepositoryException
     */
    List<T> findAll() throws RepositoryException;

    /**
     * The method for getting single instance of entity from db
     *
     * @param id identifier of entity
     * @return Optional<T>
     * @throws RepositoryException
     */
    Optional<T> findById(Long id) throws RepositoryException;

    /**
     * The method for adding single entity data in db
     *
     * @param entity Entity to insert into db {@link Entity}
     * @throws RepositoryException
     */
    void save(T entity) throws RepositoryException;

    /**
     * The method for removing single entry in db
     *
     * @param id unique identifier of entity
     * @throws RepositoryException
     */
    void delete(Long id) throws RepositoryException;

    /**
     * The method for updating single entity fields in db
     *
     * @param entity Entity with updated fields {@link Entity}
     * @throws RepositoryException
     */
    void update(T entity) throws RepositoryException;

}
