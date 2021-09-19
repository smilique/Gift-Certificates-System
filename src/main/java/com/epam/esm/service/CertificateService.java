package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.RepositoryException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateService implements CrudService<Certificate> {

    private static final Logger LOGGER = Logger.getLogger(CertificateService.class);

    private final CertificateRepository repository;

    public  CertificateService (CertificateRepository certificateRepository) {
        repository = certificateRepository;
    }

//    public CertificateService(Certificate)
    @Override
    public List<Certificate> list() throws ServiceException {
        try {
            return repository.findAll();
        } catch (RepositoryException e) {
            LOGGER.error(e);
            throw new ServiceException(e);
        }
    }

    public List<Certificate> listByTagName(String tagName) throws ServiceException {
        try {
            return repository.findByTagName(tagName);
        } catch (RepositoryException e) {
            LOGGER.error(e);
        throw new ServiceException(e);
        }
    }

    private boolean isCertificateWithNameExists(Certificate certificate) throws RepositoryException {
        String name = certificate.getName();
        Optional<Certificate> optionalCert = repository.findByName(name);
        return optionalCert.isPresent();
    }

    @Override
    public void save(Certificate certificate) throws RepositoryException {
        if (!isCertificateWithNameExists(certificate)) {
            repository.save(certificate);
            //TODO needs to be revised
        }
    }

    @Override
    public Optional<Certificate> get(Long id) throws RepositoryException {
        return repository.findById(id);
    }

    @Override
    public void update(Certificate certificate) throws RepositoryException {
        repository.update(certificate);
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        repository.delete(id);
    }
}
