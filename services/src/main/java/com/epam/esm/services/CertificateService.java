package com.epam.esm.services;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repositories.CertificateRepository;
import com.epam.esm.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public List<Certificate> getByName(String searchString) {
        return certRepository.find(searchString);
    }

    public List<Certificate> getByName(String sortByName, String sortByDate) {
        return certRepository.findAll(sortByName, sortByDate);
    }

    public List<Certificate> search(String nameSort, String dateSort, String searchString) {
        if (searchString == null && dateSort == null && nameSort == null) {
            return certRepository.findAll();
        } else if (searchString == null && dateSort == null) {
            return certRepository.findAllSortByName(nameSort);
        } else if (searchString == null && nameSort == null) {
            return certRepository.findAllSortByDate(dateSort);
        } else if (searchString == null) {
            return certRepository.findAll(nameSort, dateSort);
        } else {
            return certRepository.findAndSort(nameSort, dateSort, searchString);
        }
    }

    public List<Certificate> getByTagName(String tagName) {
        return certRepository.findByTagName(tagName);
    }

    private boolean isCertificateWithNameExists(Certificate certificate) {
        String name = certificate.getName();
        Optional<Certificate> optionalCert = certRepository.findByName(name);
        return optionalCert.isPresent();
    }

    private boolean isCertificateWithIdExists(Certificate certificate) {
        Long id = certificate.getId();
        Optional<Certificate> optionalCertificate = certRepository.findById(id);
        return optionalCertificate.isPresent();
    }

    @Override
    @Transactional
    public void save(Certificate certificate) {
        if (!isCertificateWithNameExists(certificate)) {
            List<Tag> actualTags = setActualTags(certificate);
            certRepository.createNewCertificate(certificate, actualTags);
        }
    }

    @Transactional
    public void updateExistingCertificate(Certificate certificate) {
        if (isCertificateWithIdExists(certificate)) {
            List<Tag> actualTags = setActualTags(certificate);
            certRepository.update(certificate, actualTags);
        }
    }

    private List<Tag> setActualTags(Certificate certificate) {
        List<Tag> tags = certificate.getTags();
        for (Tag tag: tags) {
            String tagName = tag.getName();
            Optional<Tag> optionalTag = tagRepository.findByName(tagName);
            if (!optionalTag.isPresent()) {
                tag.setId(tagRepository.saveNew(tag));
            } else {
                Tag entity = optionalTag.get();
                Long tagId = entity.getId();
                tag.setId(tagId);
            }
        }
        return tags;
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
}