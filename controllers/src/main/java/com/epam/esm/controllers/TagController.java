package com.epam.esm.controllers;

import com.epam.esm.entities.Tag;
import com.epam.esm.services.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Handles HTTP requests to interact with Tag entities in the database
 *
 * @author Anton Tamashevich
 * @version 1.0
 * @see com.epam.esm.services.TagService
 */

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Gets all Tag entries from db sorted by Tag id
     *
     * @return ResponseEntity<>
     * @throws JsonProcessingException
     */
    @GetMapping
    public ResponseEntity<String> getTags() throws JsonProcessingException {
        List<Tag> tags = tagService.getAll();
        return ResponseEntity.ok(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(tags));
    }

    /**
     * Get Tag by Tag id
     *
     * @param id obligatory, Tag id
     * @return ResponseEntity<>
     * @throws JsonProcessingException
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getTag(@PathVariable Long id) throws JsonProcessingException {
        Optional<Tag> optionalTag = tagService.get(id);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            return ResponseEntity.ok(objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(tag));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get Tag by Tag name
     *
     * @param name obligatory, Tag name
     * @return ResponseEntity<>
     * @throws JsonProcessingException
     */
    @GetMapping("/search/{name}")
    public ResponseEntity<String> getTagByName(@PathVariable String name) throws JsonProcessingException {
        Optional<Tag> optionalTag = tagService.get(name);
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            return ResponseEntity.ok(objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(tag));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete Tag by id
     *
     * @param id Tag id
     * @return ResponseEntity<>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.accepted().build();
    }

    /**
     * Add Certificate entity to the db
     *
     * @param tag Certificate entity in JSON format
     * @return ResponseEntity<>
     */
    @PostMapping("/add")
    public ResponseEntity<String> addTag(@RequestBody Tag tag) {
        tagService.save(tag);
        return ResponseEntity.accepted().build();
    }

}
