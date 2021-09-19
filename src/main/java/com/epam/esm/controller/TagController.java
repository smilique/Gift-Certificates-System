package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.RepositoryException;
import com.epam.esm.service.ServiceException;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Anton Tamashevich
 */

@RestController
@RequestMapping("/tags")
public class TagController {

    private static final Logger LOGGER = Logger.getLogger(TagController.class);

    private final TagService tagService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<String> getTags(@RequestBody(required = false) String body) throws ControllerException {
        try {
            List<Tag> tags = tagService.list();
            return ResponseEntity.ok(
                    objectMapper.writeValueAsString(tags));
        } catch (JsonProcessingException | ServiceException e) {
            LOGGER.error(e);
            throw new ControllerException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getTag(@PathVariable Long id) throws ControllerException {
        try {
            Optional<Tag> optionalTag = tagService.get(id);
            if (optionalTag.isPresent()) {
                Tag tag = optionalTag.get();
                return ResponseEntity.ok(
                        objectMapper.writeValueAsString(tag));
            }  else {
                return ResponseEntity.badRequest().body("");
            }
        } catch (JsonProcessingException | RepositoryException e) {
            LOGGER.error(e);
            throw new ControllerException(e);
//            return ResponseEntity.badRequest().body("IAMERROR");
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<String> getTagByName(@PathVariable String name) throws ControllerException {
        try {
            Optional<Tag> optionalTag = tagService.get(name);
            Tag tag = optionalTag.get();
            return ResponseEntity.ok(objectMapper.writeValueAsString(tag));
        } catch (RepositoryException | JsonProcessingException e) {
            LOGGER.error(e);
            throw new ControllerException(e);
//            return ResponseEntity.badRequest().body("IAMERROR");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable String id) throws ControllerException {
        try {
            Long currentId = Long.parseLong(id);
            tagService.delete(currentId);
            return ResponseEntity.ok("");
        } catch (RepositoryException e) {
            LOGGER.error(e);
            throw new ControllerException(e);
        }

    }

    @PostMapping("/add")
    public ResponseEntity<String> addTag(@RequestBody Tag tag) throws ControllerException {
        try {
            tagService.save(tag);
            return ResponseEntity.ok("");
        } catch (RepositoryException e) {
            LOGGER.error(e);
            throw new ControllerException(e);
        }

    }

//    @PutMapping("/update")
//    public ResponseEntity<String> updateTag(@RequestBody Tag tag) throws ControllerException {
//        try {
//            tagService.update(tag);
//            return ResponseEntity.ok("");
//        } catch (RepositoryException e) {
//            LOGGER.error(e);
//            throw new ControllerException(e);
//        }
//    }
}
