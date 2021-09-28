package com.epam.esm.services;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repositories.CertificateRepository;
import com.epam.esm.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CertificateService implements CrudService<Certificate> {

    private final CertificateRepository certRepository;
    private final TagRepository tagRepository;

    public CertificateService(CertificateRepository certificateRepository, TagRepository tagRepository) {
        certRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Certificate> getAll() {
        return certRepository.findAll();
    }

    public List<Certificate> list(String searchString) {
        return certRepository.find(searchString);
    }

    public List<Certificate> list(String sortByName, String sortByDate) {
        return certRepository.findAll(sortByName, sortByDate);
    }

    public List<Certificate> search(String nameSort, String dateSort, String searchString) {
        return certRepository.findAndSort(nameSort, dateSort, searchString);
    }

    public List<Certificate> listByTagName(String tagName) {
        return certRepository.findByTagName(tagName);
    }

    private boolean isCertificateWithNameExists(Certificate certificate) {
        String name = certificate.getName();
        Optional<Certificate> optionalCert = certRepository.findByName(name);
        return optionalCert.isPresent();
    }

    @Override
    public void save(Certificate certificate) {
        if (!isCertificateWithNameExists(certificate)) {
            certRepository.save(certificate);
        }
    }

    @Override
    public Optional<Certificate> get(Long id) {
        return certRepository.findById(id);
    }

    @Override
    public void update(Certificate certificate) {
        certRepository.update(certificate);
    }

    @Override
    public void delete(Long id) {
        certRepository.delete(id);
    }

    public void setTags(List<Map<String, Object>> tagKeys, Long certificateId) {
        certRepository.setTagsToCertificates(tagKeys, certificateId);
    }

    @Transactional
    public void updateExistingCertificate(Certificate certificate) {
        List<Tag> tags = certificate.getTags();
        List<Map<String, Object>> tagKeys = tagRepository.save(tags);
        Long certificateId = certificate.getId();
        setTags(tagKeys, certificateId);
        update(certificate);
    }
}