package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.Tag;
import com.epam.esm.entities.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LinkBuilder {

    public List<Link> get(User user) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUserById(user.getId()))
                .withSelfRel());
        links.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(OrderController.class)
                                .getByUserId(user.getId(), 1, 20))
                .withRel("orders"));
        return links;
    }

    public List<Link> get(Certificate certificate) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CertificateController.class)
                                .getCertificate(certificate.getId()))
                .withSelfRel());
        return links;
    }

    public List<Link> get(Order order) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(OrderController.class)
                                .getById(order.getId()))
                .withSelfRel());
        return links;
    }

    public List<Link> get(Tag tag) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(TagController.class)
                                .getTag(tag.getId()))
                .withSelfRel());
        links.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CertificateController.class)
                                .getCertificatesByTag(tag.getName(), 1, 20))
                .withRel("certificates"));
        return links;
    }
}
