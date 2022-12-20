package com.epam.esm.controllers;

import com.epam.esm.entities.User;
import com.epam.esm.services.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Handles HTTP requests to interact with User entities in the database
 *
 * @author Anton Tamashevich
 * @version 1.0
 * @see com.epam.esm.services.UserService
 * @see com.epam.esm.controllers.LinkBuilder
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LinkBuilder linkBuilder;

    public UserController(UserService userService, LinkBuilder linkBuilder) {
        this.userService = userService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Get all User entries from db
     *
     * @param currentPage  obligatory parameter, current page number
     * @param itemsPerPage obligatory parameter, maximum number of items listed per page
     * @return ResponseEntity<>
     */
    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                      @RequestParam(value = "items", defaultValue = "20") Integer itemsPerPage) {
        List<User> users = userService.getAll(currentPage, itemsPerPage);
        Link selfLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUsers(currentPage, itemsPerPage))
                .withSelfRel();
        for (User user : users) {
            List<Link> links = linkBuilder.get(user);
            user.add(links);
        }
        CollectionModel<User> model = CollectionModel.of(users);
        model.add(selfLink);
        return ResponseEntity.ok(model);
    }

    /**
     * Get single User entry from db
     *
     * @return ResponseEntity<>
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.get(id);
        return getResponseEntity(optionalUser);
    }

    /**
     * Get User by max cost of orders
     *
     * @return ResponseEntity<>
     */
    @GetMapping("/topBuyer")
    public ResponseEntity<?> getUserByHighestCostOfOrders() {
        Optional<User> optionalUser = userService.getUserWithMaxOrders();
        return getResponseEntity(optionalUser);
    }

    private ResponseEntity<?> getResponseEntity(Optional<User> optionalUser) {
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Link> userLinks = linkBuilder.get(user);
            user.add(userLinks);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
