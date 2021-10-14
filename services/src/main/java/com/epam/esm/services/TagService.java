package com.epam.esm.services;

import com.epam.esm.entities.Tag;
import com.epam.esm.repositories.TagRepository;
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
    public List<Tag> getAll(Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        return repository.findAll(startPosition, itemsPerPage);
    }

    @Override
    public void save(Tag tag) {
        repository.save(tag);
    }

    @Override
    public Optional<Tag> get(Long id) {
        return repository.findById(id);
    }

    public Optional<Tag> get(String name) {
        return repository.findByName(name);
    }

    public Optional<Tag> getMostUsedTag() {
        return repository.findMostUsedTag();
    }

    @Override
    public void update(Tag tag) {

        //unsupported operation for Tag
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
