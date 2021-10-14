package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Order;
import com.epam.esm.services.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * OrderController used to handle HTTP requests to interact with Order entities in the database
 *
 * @author Anton Tamashevich
 * @version 1.0
 * @see com.epam.esm.services.OrderService
 * @see com.epam.esm.controllers.LinkBuilder
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final LinkBuilder linkBuilder;

    public OrderController(OrderService orderService, LinkBuilder linkBuilder) {
        this.orderService = orderService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Get all Order entries from db
     *
     * @param currentPage  obligatory parameter, current page number
     * @param itemsPerPage obligatory parameter, maximum number of items listed per page
     * @return ResponseEntity<>
     */
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "page") Integer currentPage,
                                    @RequestParam(value = "items") Integer itemsPerPage) {
        List<Order> orders = orderService.getAll(currentPage, itemsPerPage);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                        .methodOn(OrderController.class)
                        .getAll(currentPage, itemsPerPage))
                .withSelfRel();
        return getResponseEntity(orders, selfLink);
    }

    /**
     * Get Order entry by id
     *
     * @param id Order id
     * @return ResponseEntity<>
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderService.get(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            List<Link> orderLinks = linkBuilder.get(order);
            order.add(orderLinks);
            Certificate certificate = order.getCertificate();
            List<Link> certificateLinks = linkBuilder.get(certificate);
            certificate.add(certificateLinks);
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get Orders by User id
     *
     * @param userId       User id
     * @param currentPage  obligatory parameter, current page number
     * @param itemsPerPage obligatory parameter, maximum number of items listed per page
     * @return ResponseEntity<>
     */
    @GetMapping("/get")
    public ResponseEntity<?> getByUserId(@RequestParam("userId") Long userId,
                                         @RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                         @RequestParam(value = "items", defaultValue = "20") Integer itemsPerPage) {
        List<Order> orders = orderService.getByUserId(userId, currentPage, itemsPerPage);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                        .methodOn(OrderController.class)
                        .getByUserId(userId, currentPage, itemsPerPage))
                .withSelfRel();
        return getResponseEntity(orders, selfLink);
    }

    private ResponseEntity<?> getResponseEntity(List<Order> orders, Link selfLink) {
        for (Order order : orders) {
            List<Link> orderLinks = linkBuilder.get(order);
            order.add(orderLinks);
            Certificate certificate = order.getCertificate();
            List<Link> certificateLinks = linkBuilder.get(certificate);
            certificate.add(certificateLinks);
        }
        CollectionModel<Order> model = CollectionModel.of(orders, selfLink);
        return ResponseEntity.ok(model);
    }

}
