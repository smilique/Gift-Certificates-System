package com.epam.esm.controllers;

import com.epam.esm.entities.Tag;
import com.epam.esm.services.TagService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
 * @see com.epam.esm.controllers.LinkBuilder
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final LinkBuilder linkBuilder;

    public TagController(TagService tagService, LinkBuilder linkBuilder) {
        this.tagService = tagService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Gets all Tag entries from db sorted by Tag id
     *
     * @param currentPage  obligatory parameter, current page number
     * @param itemsPerPage obligatory parameter, maximum number of items listed per page
     * @return ResponseEntity<>
     */
    @GetMapping
    public ResponseEntity<?> getTags(@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                     @RequestParam(value = "items", defaultValue = "20") Integer itemsPerPage) {
        List<Tag> tags = tagService.getAll(currentPage, itemsPerPage);
        for (Tag tag : tags) {
            List<Link> links = linkBuilder.get(tag);
            tag.add(links);
        }
        Link self = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(TagController.class)
                                .getTags(currentPage, itemsPerPage))
                .withSelfRel();
        CollectionModel<Tag> model = CollectionModel.of(tags, self);
        return ResponseEntity.ok(model);
    }

    /**
     * Get Tag by Tag id
     *
     * @param id obligatory, Tag id
     * @return ResponseEntity<>
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTag(@PathVariable Long id) {
        Optional<Tag> optionalTag = tagService.get(id);
        return getResponseEntity(optionalTag);
    }

    /**
     * Get Tag by Tag name
     *
     * @param name obligatory, Tag name
     * @return ResponseEntity<>
     */
    @GetMapping("/search/{name}")
    public ResponseEntity<?> getTagByName(@PathVariable String name) {
        Optional<Tag> optionalTag = tagService.get(name);
        return getResponseEntity(optionalTag);
    }

    /**
     * Delete Tag by id
     *
     * @param id Tag id
     * @return ResponseEntity<>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.accepted()
                .build();
    }

    /**
     * Add Tag entity to the db
     *
     * @param tag Tag entity in JSON
     * @return ResponseEntity<>
     */
    @PostMapping("")
    public ResponseEntity<?> addTag(@RequestBody Tag tag) {
        tagService.save(tag);
        return ResponseEntity.accepted()
                .build();
    }

    /**
     * Get most used Tag entity of User with maximal cost of orders
     *
     * @return ResponseEntity<>
     */
    @GetMapping("/topTag")
    public ResponseEntity<?> getMostUsedTagOfTopBuyer() {
        Optional<Tag> optionalTag = tagService.getMostUsedTag();
        return getResponseEntity(optionalTag);
    }

    private ResponseEntity<?> getResponseEntity(Optional<Tag> optionalTag) {
        if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            List<Link> links = linkBuilder.get(tag);
            tag.add(links);
            return ResponseEntity.ok(tag);
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

}
