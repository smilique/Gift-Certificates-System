package com.epam.esm.repositories;

import com.epam.esm.entities.Certificate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateRepository implements CrudRepositoryInterface<Certificate> {

    private static final String SELECT_ALL = "from Certificate cert ";
    private static final String ORDER_BY_NAME = " order by cert.name ";
    private static final String ORDER_BY_DATE = ", cert.createDate ";
    private static final String SELECT_BY_ID = "from Certificate where id = :id ";
    private static final String SELECT_BY_NAME = "from Certificate where name = :name ";
    private static final String SELECT_ALL_ORDER_NAME = "from Certificate cert order by cert.name ";
    private static final String SELECT_ALL_ORDER_DATE = "from Certificate cert order by cert.createDate ";
    private static final String SELECT_BY_TAG_NAMES = "select cert from Certificate cert join cert.tags tag where tag.name in :tagNames";
    private static final String LIKE = "where cert.name like :searchString or cert.description like :searchString ";
    private static final String DELETE_BY_ID = "delete from Certificate where id = :id";
    private static final String SET_NEW_TAGS = "update Certificate.tags as t set t = :tags";
    private static final String UPDATE_CERTIFICATE = "update Certificate set name = :name, description = :description, price = :price," +
            " duration = :duration, lastUpdateDate = :lastUpdateDate where id = :id";
    private static final String UPDATE_CERTIFICATE_PROPERTY = "update Certificate set ";
    private static final String UPDATE_PROPERTY = " = :value, lastUpdateDate = :lastUpdateDate where id = :id";

    private static final String SEARCH_PARAMETER = "searchString";

    @PersistenceContext
    private EntityManager entityManager;

    public CertificateRepository() {
    }

    @Override
    public List<Certificate> findAll(Integer startPosition, Integer itemsPerPage) {
        Query query = entityManager.createQuery(SELECT_ALL);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        Query query = entityManager.createQuery(SELECT_BY_ID);
        query.setParameter("id", id);
        Certificate certificate = (Certificate) query.getSingleResult();
        return Optional.of(certificate);
    }

    public Optional<Certificate> findByName(String name) {
        Query query = entityManager.createQuery(SELECT_BY_NAME);
        query.setParameter("name", name);
        Certificate certificate = (Certificate) query.getSingleResult();
        return Optional.of(certificate);
    }

    public List<Certificate> findByTagName(List<String> names, Integer startPosition, Integer itemsPerPage) {
        Query query = entityManager.createQuery(SELECT_BY_TAG_NAMES);
        query.setParameter("tagNames", names);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> findAll(String nameSort, String dateSort, Integer startPosition, Integer itemsPerPage) {
        Query query = entityManager.createQuery(SELECT_ALL + ORDER_BY_NAME + nameSort + ORDER_BY_DATE + dateSort);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> findAllSortByName(String nameSort, Integer startPosition, Integer itemsPerPage) {
        String queryString = nameSort.equalsIgnoreCase("desc") ?
                SELECT_ALL_ORDER_NAME + "desc" : SELECT_ALL_ORDER_NAME;
        Query query = entityManager.createQuery(queryString);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> findAllSortByDate(String dateSort, Integer startPosition, Integer itemsPerPage) {
        String queryString = dateSort.equalsIgnoreCase("desc") ?
                SELECT_ALL_ORDER_DATE + "desc" : SELECT_ALL_ORDER_DATE;
        Query query = entityManager.createQuery(queryString);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> findAndSort(String nameSort, String dateSort, String searchString, Integer startPosition, Integer itemsPerPage) {
        Query query = entityManager.createQuery(SELECT_ALL + LIKE + ORDER_BY_NAME + nameSort + ORDER_BY_DATE + dateSort);
        query.setParameter(SEARCH_PARAMETER, "%" + searchString + "%");
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> find(String searchString, Integer startPosition, Integer itemsPerPage) {
        Query query = entityManager.createQuery(SELECT_ALL + LIKE);
        query.setParameter(SEARCH_PARAMETER, "%" + searchString + "%");
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    @Override
    @Transactional
    public void save(Certificate certificate) {
        entityManager.persist(certificate);
        entityManager.flush();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Query query = entityManager.createQuery(DELETE_BY_ID);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void update(Certificate certificate) {
        Query query = entityManager.createQuery(UPDATE_CERTIFICATE);
        query.setParameter("name", certificate.getName());
        query.setParameter("description", certificate.getDescription());
        query.setParameter("price", certificate.getPrice());
        query.setParameter("duration", certificate.getDuration());
        certificate.setLastUpdateDate(ZonedDateTime.now());
        query.setParameter("lastUpdateDate", certificate.getLastUpdateDate());
        query.setParameter("id", certificate.getId());
        query.executeUpdate();
        Query tagsQuery = entityManager.createQuery(SET_NEW_TAGS);
        tagsQuery.setParameter("tags", certificate.getTags());
        tagsQuery.executeUpdate();
    }

    @Transactional
    public void updateProperty(Long id, String property, String value) {
        Query query = entityManager.createQuery(UPDATE_CERTIFICATE_PROPERTY + property + UPDATE_PROPERTY);
        query.setParameter("value", value);
        query.setParameter("lastUpdateDate", ZonedDateTime.now());
        query.setParameter("id", id);
        query.executeUpdate();
    }

}