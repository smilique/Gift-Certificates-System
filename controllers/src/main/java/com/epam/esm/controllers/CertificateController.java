package com.epam.esm.controllers;

import com.epam.esm.entities.Certificate;
import com.epam.esm.services.CertificateService;
import com.epam.esm.services.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * CertificateController used to handle HTTP requests to interact with Certificate entities in the database
 *
 * @author Anton Tamashevich
 * @version 1.0
 * @see com.epam.esm.services.CertificateService
 * @see com.epam.esm.services.TagService
 */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private static final Logger LOGGER = Logger.getLogger(CertificateController.class);

    private final CertificateService certificateService;
    private final TagService tagService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CertificateController(CertificateService certificateService, TagService tagService) {
        this.certificateService = certificateService;
        this.tagService = tagService;
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Gets all Certificate entries from db
     * Can be supplemented with sorting and search parameters
     * Call without parameters will return Certificate entries sorted by Certificate id
     *
     * @param sortByName   optional parameter, sort entries by Certificate name if needed,
     * @param sortByDate   optional parameter, sort entries by Certificate create date if needed
     * @param searchString optional parameter, search entries by Certificate name or description if needed
     * @return ResponseEntity<>
     */
    @GetMapping
    public ResponseEntity<String> getCertificates(@RequestParam(value = "sortByName", required = false) String sortByName,
                                                  @RequestParam(value = "sortByDate", required = false) String sortByDate,
                                                  @RequestParam(value = "search", required = false) String searchString) throws JsonProcessingException {
        List<Certificate> certificates;
        certificates = certificateService.search(sortByName, sortByDate, searchString);
        return ResponseEntity.ok(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(certificates));
    }

    /**
     * Search with the name or description like requested in param
     *
     * @param searchString obligatory parameter, search entries by Certificate name or description
     * @return ResponseEntity<>
     */
    @GetMapping("/search")
    public ResponseEntity<String> searchCertificates(@RequestParam(value = "value") String searchString) {
        List<Certificate> certificates = certificateService.getByName(searchString);
        try {
            return ResponseEntity.ok(objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(certificates));
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    /**
     * Search with the name or description like requested in param
     *
     * @param tagName obligatory parameter, search Certificates by name of Tag
     * @return ResponseEntity<>
     */
    @GetMapping("/get")
    public ResponseEntity<String> getCertificatesByTag(@RequestParam("tagName") String tagName) {
        try {
            List<Certificate> certificates = certificateService.getByTagName(tagName);
            return ResponseEntity.ok(
                    objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(certificates));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    /**
     * Get Certificate by Certificate id
     *
     * @param id obligatory, Certificate id
     * @return ResponseEntity<>
     * @throws JsonProcessingException
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getCertificate(@PathVariable Long id) throws JsonProcessingException {
        Optional<Certificate> optionalCertificate = certificateService.get(id);
        if (optionalCertificate.isPresent()) {
            Certificate certificate = optionalCertificate.get();
            return ResponseEntity.ok(objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(certificate));
        } else {
            return ResponseEntity.badRequest()
                    .build();
        }
    }

    /**
     * Delete Certificate by id
     *
     * @param id Certificate id
     * @return ResponseEntity<>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable Long id) {
        certificateService.delete(id);
        return ResponseEntity.accepted()
                .build();
    }

    /**
     * Add Certificate entity to the db
     *
     * @param body Certificate entity in JSON format
     * @return ResponseEntity<>
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCertificate(@RequestBody String body) {
        Certificate certificate = new Gson().fromJson(body, Certificate.class);
        certificateService.save(certificate);
        return ResponseEntity.accepted()
                .build();
    }

    /**
     * Update certificate entity in the db
     *
     * @param body Certificate entity in JSON format
     * @return ResponseEntity<>
     */
    @PutMapping("/update")
    public ResponseEntity<String> updateCertificate(@RequestBody String body) {
        Certificate certificate = new Gson().fromJson(body, Certificate.class);
        certificateService.updateExistingCertificate(certificate);
        return ResponseEntity.accepted()
                .build();
    }
}
