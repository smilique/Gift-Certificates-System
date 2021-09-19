package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.RepositoryException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Anton Tamashevich
 */

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @GetMapping
    public ResponseEntity<String> getCertificates() throws ServiceException {
        try {
            List<Certificate> certificates = certificateService.list();
            return ResponseEntity.ok(
                    objectMapper.writeValueAsString(certificates));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("JsonProcessingException occurred");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<String> getCertificates(@RequestParam("tagName") String tagName) throws ServiceException {
        try {
            List<Certificate> certificates = certificateService.listByTagName(tagName);
            return ResponseEntity.ok(
                    objectMapper.writeValueAsString(certificates));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Json proc Exception");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCertificate(@PathVariable Long id) throws ControllerException, JsonProcessingException, RepositoryException {
        Optional<Certificate> optionalCertificate = certificateService.get(id);
        if (optionalCertificate.isPresent()) {
            Certificate certificate = optionalCertificate.get();
            return ResponseEntity.ok(
                    objectMapper.writeValueAsString(certificate));
        } else {
            return ResponseEntity.badRequest().body("");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable Long id) throws RepositoryException {
        certificateService.delete(id);
        return ResponseEntity.ok("");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCertificate(@RequestBody String body) throws RepositoryException {
        Certificate certificate = new Gson().fromJson(body, Certificate.class);
        certificateService.save(certificate);
        return ResponseEntity.ok("");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCertificate(@RequestBody String body) throws RepositoryException {
        Certificate certificate = new Gson().fromJson(body, Certificate.class);
        certificateService.update(certificate);
        return ResponseEntity.ok("");
    }
}
