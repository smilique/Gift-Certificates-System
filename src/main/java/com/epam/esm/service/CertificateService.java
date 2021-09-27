package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.CertificateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CertificateService implements CrudService<Certificate> {

    private final CertificateRepository repository;

    public CertificateService(CertificateRepository certificateRepository) {
        repository = certificateRepository;
    }

    @Override
    public List<Certificate> getAll() {
        return repository.findAll();
    }

    public List<Certificate> list(String searchString) {
        return repository.find(searchString);
    }

    public List<Certificate> list(String sortByName, String sortByDate) {
        return repository.findAll(sortByName, sortByDate);
    }

    public List<Certificate> search(String nameSort, String dateSort, String searchString) {
        return repository.findAndSort(nameSort, dateSort, searchString);
    }

    public List<Certificate> listByTagName(String tagName) {
        return repository.findByTagName(tagName);
    }

    private boolean isCertificateWithNameExists(Certificate certificate) {
        String name = certificate.getName();
        Optional<Certificate> optionalCert = repository.findByName(name);
        return optionalCert.isPresent();
    }

    @Override
    public void save(Certificate certificate) {
        if (!isCertificateWithNameExists(certificate)) {
            repository.save(certificate);
        }
    }

    @Override
    public Optional<Certificate> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public void update(Certificate certificate) {
        repository.update(certificate);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    public void setTags(List<Map<String, Object>> tagKeys, Long certificateId) {
        repository.setTagsToCertificates(tagKeys, certificateId);
    }
}
