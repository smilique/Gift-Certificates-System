package com.epam.esm.services;

import com.epam.esm.entities.Certificate;
import com.epam.esm.repositories.CertificateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService implements CrudService<Certificate> {

    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_PRICE = "price";
    private static final String PARAMETER_DURATION = "duration";

    private final CertificateRepository certRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        certRepository = certificateRepository;
    }

    @Override
    public List<Certificate> getAll(Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        return certRepository.findAll(startPosition, itemsPerPage);
    }

    public List<Certificate> getByName(String searchString, Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        return certRepository.find(searchString, startPosition, itemsPerPage);
    }

    public List<Certificate> getByName(String sortByName, String sortByDate, Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        return certRepository.findAll(sortByName, sortByDate, startPosition, itemsPerPage);
    }

    public List<Certificate> search(String nameSort, String dateSort, String searchString, Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        if (searchString == null && dateSort == null && nameSort == null) {
            return certRepository.findAll(startPosition, itemsPerPage);
        } else if (searchString == null && dateSort == null) {
            return certRepository.findAllSortByName(nameSort, startPosition, itemsPerPage);
        } else if (searchString == null && nameSort == null) {
            return certRepository.findAllSortByDate(dateSort, startPosition, itemsPerPage);
        } else if (searchString == null) {
            return certRepository.findAll(nameSort, dateSort, startPosition, itemsPerPage);
        } else if (nameSort == null && dateSort == null) {
            return certRepository.find(searchString, startPosition, itemsPerPage);
        } else {
            return certRepository.findAndSort(nameSort, dateSort, searchString, startPosition, itemsPerPage);
        }
    }

    public List<Certificate> getByTagName(List<String> tagNames, Integer currentPage, Integer itemsPerPage) {
        Integer startPosition = currentPage * itemsPerPage - itemsPerPage;
        return certRepository.findByTagName(tagNames, startPosition, itemsPerPage);
    }

    @Override
    @Transactional
    public void save(Certificate certificate) {

        certRepository.save(certificate);
    }

    @Transactional
    public void updateExistingCertificate(Certificate certificate) {
        certRepository.update(certificate);
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

    public void updateExistingCertificate(Long id, String name, String description, BigDecimal price, Long duration) {
        if (name != null) {
            certRepository.updateProperty(id, PARAMETER_NAME, name);
        } else if (description != null) {
            certRepository.updateProperty(id, PARAMETER_DESCRIPTION, description);
        } else if (price != null) {
            certRepository.updateProperty(id, PARAMETER_PRICE, price.toString());
        } else if (duration != null) {
            certRepository.updateProperty(id, PARAMETER_DURATION, duration.toString());
        }
    }
}