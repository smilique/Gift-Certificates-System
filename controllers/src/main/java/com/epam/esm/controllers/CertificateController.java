package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.services.CertificateService;
import com.google.gson.Gson;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * CertificateController used to handle HTTP requests to interact with Certificate entities in the database
 *
 * @author Anton Tamashevich
 * @version 1.0
 * @see com.epam.esm.services.CertificateService
 * @see com.epam.esm.controllers.LinkBuilder
 */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;
    private final LinkBuilder linkBuilder;

    public CertificateController(CertificateService certificateService, LinkBuilder linkBuilder) {
        this.certificateService = certificateService;
        this.linkBuilder = linkBuilder;
    }

    /**
     * Get all Certificate entries from db
     * Can be supplemented with sorting and search parameters
     * Call without parameters will return Certificate entries sorted by Certificate id
     *
     * @param sortByName   optional parameter, sort entries by Certificate name if needed,
     * @param sortByDate   optional parameter, sort entries by Certificate create date if needed
     * @param searchString optional parameter, search entries by Certificate name or description if needed
     * @param currentPage  obligatory parameter, current page number
     * @param itemsPerPage obligatory parameter, maximum number of items listed per page
     * @return ResponseEntity<>
     */
    @GetMapping
    public ResponseEntity<?> getCertificates(@RequestParam(value = "sortByName", required = false) String sortByName,
                                             @RequestParam(value = "sortByDate", required = false) String sortByDate,
                                             @RequestParam(value = "search", required = false) String searchString,
                                             @RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                             @RequestParam(value = "items", defaultValue = "20") Integer itemsPerPage) {
        List<Certificate> certificates;
        certificates = certificateService.search(sortByName, sortByDate, searchString, currentPage, itemsPerPage);
        for (Certificate certificate : certificates) {
            List<Link> links = linkBuilder.get(certificate);
            certificate.add(links);
        }
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                        .methodOn(CertificateController.class)
                        .getCertificates(sortByName, sortByDate, searchString, currentPage, itemsPerPage))
                .withSelfRel();
        CollectionModel<Certificate> model = CollectionModel.of(certificates, selfLink);
        return ResponseEntity.ok(model);
    }

    /**
     * Search with the name or description like requested in param
     *
     * @param searchString obligatory parameter, search entries by Certificate name or description
     * @param currentPage  obligatory parameter, current page number
     * @param itemsPerPage obligatory parameter, maximum number of items listed per page
     * @return ResponseEntity<>
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchCertificates(@RequestParam(value = "value") String searchString,
                                                @RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                                @RequestParam(value = "items", defaultValue = "20") Integer itemsPerPage) {

        List<Certificate> certificates = certificateService.getByName(searchString, currentPage, itemsPerPage);
        for (Certificate certificate : certificates) {
            List<Link> links = linkBuilder.get(certificate);
            certificate.add(links);
        }
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                        .methodOn(CertificateController.class)
                        .searchCertificates(searchString, currentPage, itemsPerPage))
                .withSelfRel();
        CollectionModel<Certificate> model = CollectionModel.of(certificates, selfLink);
        return ResponseEntity.ok(model);
    }

    /**
     * Search by the name of Tag or multiple names
     *
     * @param tagNames     obligatory parameter, search Certificates by name of Tag or multiple Tags
     * @param currentPage  obligatory parameter, current page number
     * @param itemsPerPage obligatory parameter, maximum number of items listed per page
     * @return ResponseEntity<>
     */
    @GetMapping("/searchByTags")
    public ResponseEntity<?> search(@RequestParam(value = "tag") List<String> tagNames,
                                    @RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                    @RequestParam(value = "items", defaultValue = "20") Integer itemsPerPage) {
        List<Certificate> certificates = certificateService.getByTagName(tagNames, currentPage, itemsPerPage);
        for (Certificate certificate : certificates) {
            List<Link> links = linkBuilder.get(certificate);
            certificate.add(links);
        }
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                        .methodOn(CertificateController.class)
                        .search(tagNames, currentPage, itemsPerPage))
                .withSelfRel();
        CollectionModel<Certificate> model = CollectionModel.of(certificates, selfLink);
        return ResponseEntity.ok(model);
    }

    /**
     * Search by the name of Tag
     *
     * @param tagName      obligatory parameter, search Certificates by name of Tag
     * @param currentPage  obligatory parameter, current page number
     * @param itemsPerPage obligatory parameter, maximum number of items listed per page
     * @return ResponseEntity<>
     */
    @GetMapping("/get")
    public ResponseEntity<?> getCertificatesByTag(@RequestParam("tagName") String tagName,
                                                  @RequestParam(value = "page", defaultValue = "1") Integer currentPage,
                                                  @RequestParam(value = "items", defaultValue = "20") Integer itemsPerPage) {
        return search(Collections.singletonList(tagName), currentPage, itemsPerPage);
    }

    /**
     * Get Certificate by Certificate id
     *
     * @param id obligatory, Certificate id
     * @return ResponseEntity<>
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCertificate(@PathVariable Long id) {
        Optional<Certificate> optionalCertificate = certificateService.get(id);
        if (optionalCertificate.isPresent()) {
            Certificate certificate = optionalCertificate.get();
            List<Link> links = linkBuilder.get(certificate);
            certificate.add(links);
            List<Tag> tags = certificate.getTags();
            for (Tag tag : tags) {
                List<Link> tagLinks = linkBuilder.get(tag);
                tag.add(tagLinks);
            }
            return ResponseEntity.ok(certificate);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete Certificate by id
     *
     * @param id Certificate id
     * @return ResponseEntity<>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable Long id) {
        certificateService.delete(id);
        return ResponseEntity.accepted().build();
    }

    /**
     * Add Certificate entity to the db
     *
     * @param body Certificate entity in JSON format
     * @return ResponseEntity<>
     */
    @PostMapping()
    public ResponseEntity<?> addCertificate(@RequestBody String body) {
        Certificate certificate = new Gson().fromJson(body, Certificate.class);
        certificateService.save(certificate);
        return ResponseEntity.accepted().build();
    }

    /**
     * Update certificate entity in the db
     *
     * @param body Certificate entity in JSON format
     * @return ResponseEntity<>
     */
    @PutMapping()
    public ResponseEntity<?> updateCertificate(@RequestBody String body) {
        Certificate certificate = new Gson().fromJson(body, Certificate.class);
        certificateService.updateExistingCertificate(certificate);
        return ResponseEntity.accepted().build();
    }

    /**
     * Update single field of certificate entity in the db
     *
     * @param name        Certificate new name
     * @param description Certificate new description
     * @param price       Certificate new price
     * @param duration    Certificate new duration
     * @return ResponseEntity<>
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificateField(@PathVariable Long id,
                                                    @RequestParam(required = false, value = "name") String name,
                                                    @RequestParam(required = false, value = "description") String description,
                                                    @RequestParam(required = false, value = "price") BigDecimal price,
                                                    @RequestParam(required = false, value = "duration") Long duration) {
        certificateService.updateExistingCertificate(id, name, description, price, duration);
        return ResponseEntity.accepted().build();
    }
}
