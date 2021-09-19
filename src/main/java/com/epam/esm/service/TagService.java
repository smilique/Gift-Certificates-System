package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.RepositoryException;
import com.epam.esm.repository.TagRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService implements CrudService<Tag> {

    private static final Logger LOGGER = Logger.getLogger(TagService.class);

    private final TagRepository repository;

    public TagService (TagRepository tagRepository) {
        repository = tagRepository;
    }


    @Override
    public List<Tag> list() throws ServiceException {
        try {
            return repository.findAll();
        } catch (RepositoryException e) {
            LOGGER.error(e);
            throw new ServiceException(e);
        }
    }

    private boolean isTagWithNameExists(Tag tag) throws RepositoryException {
        String name = tag.getName();
        Optional<Tag> optionalTag = repository.findByName(name);
        return optionalTag.isPresent();
    }

    @Override
    public void save(Tag tag) throws RepositoryException {
        if (!isTagWithNameExists(tag)) {
            repository.save(tag);
        }
    }

    @Override
    public Optional<Tag> get(Long id) throws RepositoryException {
        return repository.findById(id);
    }


    public Optional<Tag> get(String name) throws RepositoryException {
        return repository.findByName(name);
    }

    @Override
    public void update(Tag tag) throws RepositoryException {
        repository.update(tag);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        repository.delete(id);
    }
}
