package com.epam.esm.services;

import com.epam.esm.entities.Tag;
import com.epam.esm.repositories.TagRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService implements CrudService<Tag> {

    private final TagRepository repository;

    public TagService(TagRepository tagRepository) {
        repository = tagRepository;
    }

    @Override
    public List<Tag> getAll() {
        return repository.findAll();
    }

    private boolean isTagWithNameExists(Tag tag) {
        String name = tag.getName();
        Optional<Tag> optionalTag = repository.findByName(name);
        return optionalTag.isPresent();
    }

    @Override
    public void save(Tag tag) {
        if (!isTagWithNameExists(tag)) {
            repository.save(tag);
        }
    }

    @Override
    public Optional<Tag> get(Long id) {
        return repository.findById(id);
    }

    public Optional<Tag> get(String name) {
        return repository.findByName(name);
    }

    @Override
    public void update(Tag tag) {

        //unnecessary operation for Tag
        //empty method to implement CrudService interface
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
