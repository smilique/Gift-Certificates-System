package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.RepositoryException;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    List<T> list() throws ServiceException;
    void save(T t) throws RepositoryException;
    Optional<T> get(Long id) throws RepositoryException;
    void update(T t) throws RepositoryException;
    void delete(Long id) throws RepositoryException;

}
