package com.epam.esm.repositories;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateRepository implements CrudRepositoryInterface<Certificate> {

    private static final String SELECT_ALL = "from Certificate cert ";
    private static final String ORDER_BY_NAME = " order by cert.name ";
    private static final String ORDER_BY_DATE = ", cert.createDate ";
    private static final String SELECT_BY_ID = "from Certificate where id = :id ";
    private static final String SELECT_ALL_ORDER_NAME = "from Certificate cert order by cert.name ";
    private static final String SELECT_ALL_ORDER_DATE = "from Certificate cert order by cert.createDate ";
    private static final String SELECT_BY_TAG_NAMES = "select cert from Certificate cert join cert.tags tag where tag.name in :tagNames";
    private static final String INSERT_TAGGED_CERTIFICATE = "insert into tagged_gift_certificate (gift_certificate_id, tag_id) values ";
    private static final String LIKE = "where cert.name like :searchString or cert.description like :searchString ";
    private static final String DELETE_BY_ID = "delete from Certificate where id = :id";
    private static final String CLEAR_CERTIFICATE_TAGS = "delete from tagged_gift_certificate where gift_certificate_id = :giftCertificateId";
    private static final String GET_TAGS_ID = "from Tag where name = :name";
    private static final String GET_LAST_CERTIFICATE_ID = "select id from Certificate order by id desc";
    private static final String ADD_CERTIFICATE = "insert into gift_certificate set name = :name, description = :description, price = :price," +
            " duration = :duration, create_date = :createDate, last_update_date = :lastUpdateDate";
    private static final String UPDATE_CERTIFICATE = "update Certificate set name = :name, description = :description, price = :price," +
            " duration = :duration, lastUpdateDate = :lastUpdateDate where id = :id";
    private static final String UPDATE_CERTIFICATE_PROPERTY = "update Certificate set ";
    private static final String UPDATE_PROPERTY = " = :value, lastUpdateDate = :lastUpdateDate where id = :id";
    private static final String VALUE_BLOCK_START = "(";
    private static final String VALUE_BLOCK_HAS_NEXT = "), ";
    private static final String VALUE_DELIMITER = ", ";
    private static final String VALUE_BLOCK_END = ")";

    private static final String SEARCH_PARAMETER = "searchString";

    @PersistenceContext
    private Session session;

    public CertificateRepository() {
    }

    @Override
    public List<Certificate> findAll(Integer startPosition, Integer itemsPerPage) {
        Query query = session.createQuery(SELECT_ALL);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        Query query = session.createQuery(SELECT_BY_ID);
        query.setParameter("id", id);
        Certificate certificate = (Certificate) query.getSingleResult();
        return Optional.of(certificate);
    }

    public List<Certificate> findByTagName(List<String> names, Integer startPosition, Integer itemsPerPage) {
        Query query = session.createQuery(SELECT_BY_TAG_NAMES);
        query.setParameter("tagNames", names);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> findAll(String nameSort, String dateSort, Integer startPosition, Integer itemsPerPage) {
        Query query = session.createQuery(SELECT_ALL + ORDER_BY_NAME + nameSort + ORDER_BY_DATE + dateSort);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> findAllSortByName(String nameSort, Integer startPosition, Integer itemsPerPage) {
        String queryString = nameSort.equalsIgnoreCase("desc") ?
                SELECT_ALL_ORDER_NAME + " desc " : SELECT_ALL_ORDER_NAME;
        Query query = session.createQuery(queryString);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> findAllSortByDate(String dateSort, Integer startPosition, Integer itemsPerPage) {
        String queryString = dateSort.equalsIgnoreCase("desc") ?
                SELECT_ALL_ORDER_DATE + "desc " : SELECT_ALL_ORDER_DATE;
        Query query = session.createQuery(queryString);
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> findAndSort(String nameSort, String dateSort, String searchString, Integer startPosition, Integer itemsPerPage) {
        Query query = session.createQuery(SELECT_ALL + LIKE + ORDER_BY_NAME + nameSort + ORDER_BY_DATE + dateSort);
        query.setParameter(SEARCH_PARAMETER, "%" + searchString + "%");
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    public List<Certificate> find(String searchString, Integer startPosition, Integer itemsPerPage) {
        Query query = session.createQuery(SELECT_ALL + LIKE);
        query.setParameter(SEARCH_PARAMETER, "%" + searchString + "%");
        query.setMaxResults(itemsPerPage);
        query.setFirstResult(startPosition);
        List<Certificate> certificates = query.getResultList();
        return certificates;
    }

    @Override
    @Transactional
    public void save(Certificate certificate) {
        Query query = session.createNativeQuery(ADD_CERTIFICATE);
        query.setParameter("name", certificate.getName());
        query.setParameter("description", certificate.getDescription());
        query.setParameter("price", certificate.getPrice());
        query.setParameter("duration", certificate.getDuration());
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.systemDefault());
        query.setParameter("createDate", currentTime);
        query.setParameter("lastUpdateDate", currentTime);
        query.executeUpdate();
        Query getCertificateId = session.createQuery(GET_LAST_CERTIFICATE_ID, Long.class);
        getCertificateId.setMaxResults(1);
        Long certificateId = (long) getCertificateId.getSingleResult();
        certificate.setId(certificateId);
        List<Tag> tags = getTags(certificate);
        StringBuilder builder = new StringBuilder();
        builder.append(INSERT_TAGGED_CERTIFICATE);
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            builder.append(VALUE_BLOCK_START);
            builder.append(certificateId);
            builder.append(VALUE_DELIMITER);
            builder.append(tag.getId());
            if (i + 1 == tags.size()) {
                builder.append(VALUE_BLOCK_END);
            } else {
                builder.append(VALUE_BLOCK_HAS_NEXT);
            }
        }
        String setTagsQuery = builder.toString();
        Query setCertificateTags = session.createNativeQuery(setTagsQuery);
        setCertificateTags.executeUpdate();
    }

    @Override
    @Transactional
    public void update(Certificate certificate) {
        Query query = session.createQuery(UPDATE_CERTIFICATE);
        query.setParameter("name", certificate.getName());
        query.setParameter("description", certificate.getDescription());
        query.setParameter("price", certificate.getPrice());
        query.setParameter("duration", certificate.getDuration());
        ZonedDateTime updateTime = ZonedDateTime.now(ZoneId.systemDefault());
        query.setParameter("lastUpdateDate", updateTime);
        query.setParameter("id", certificate.getId());
        query.executeUpdate();
        clearCertificateTags(certificate.getId());
        List<Tag> tags = getTags(certificate);
        StringBuilder builder = new StringBuilder();
        builder.append(INSERT_TAGGED_CERTIFICATE);
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            builder.append(VALUE_BLOCK_START);
            builder.append(certificate.getId());
            builder.append(VALUE_DELIMITER);
            builder.append(tag.getId());
            if (i + 1 == tags.size()) {
                builder.append(VALUE_BLOCK_END);
            } else {
                builder.append(VALUE_BLOCK_HAS_NEXT);
            }
        }
        String setTagsQuery = builder.toString();
        Query setCertificateTags = session.createNativeQuery(setTagsQuery);
        setCertificateTags.executeUpdate();
    }

    private List<Tag> getTags(Certificate certificate) {
        List<Tag> tags = certificate.getTags();
        for (Tag tag : tags) {
            Query getTagsId = session.createQuery(GET_TAGS_ID); 
            getTagsId.setParameter("name", tag.getName());
            List<Tag> identifier = getTagsId.getResultList();
            if (identifier.isEmpty()) {
                Long savedTagId = (Long) session.save(tag);
                tag.setId(savedTagId);
            } else {
                Long tagId = identifier.get(0).getId();
                tag.setId(tagId);
            }
        }
        return tags;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Query query = session.createQuery(DELETE_BY_ID);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Transactional
    public void updateProperty(Long id, String property, String value) {
        Query query = session.createQuery(UPDATE_CERTIFICATE_PROPERTY + property + UPDATE_PROPERTY);
        query.setParameter("value", value);
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault());
        query.setParameter("lastUpdateDate", dateTime);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    private void clearCertificateTags(Long certificateId) {
        Query deleteRelationship = session.createNativeQuery(CLEAR_CERTIFICATE_TAGS);
        deleteRelationship.setParameter("giftCertificateId", certificateId);
        deleteRelationship.executeUpdate();
    }

}