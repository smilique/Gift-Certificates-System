package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateRepository implements CrudRepositoryInterface<Certificate>  {

    private static final Logger LOGGER = Logger.getLogger(CertificateRepository.class);

    private static final String SELECT_ALL = "select * from gift_certificate";
    private static final String SELECT_BY_TAG_NAME = "select gc.id, gc.name, gc.description, gc.price, gc.duration, " +
            "gc.create_date, gc.last_update_date from gift_certificate gc " +
            "join tagged_gift_certificate tgc on gc.id = tgc.gift_certificate_id " +
            "join tag on tag.id = tgc.tag_id where tag.name = ?";
    private static final String SELECT = "select * from gift_certificate ";
    private static final String SELECT_WITH_TAGS = "select gift_certificate.name";
    private static final String SELECT_CERTIFICATE_TAGS = "select tag.id, tag.name from tag " +
            "join tagged_gift_certificate tgc on tag.id = tgc.tag_id where tgc.gift_certificate_id = ?";
    private static final String DELETE_BY_ID = "delete from gift_certificate where id = ?";
    private static final String SAVE_NEW = "insert into gift_certificate set name = ?, description = ?, price = ?, duration = ?, " +
            "create_date = ?, last_update_date = ? ";
    private static final String UPDATE = "update gift_certificate set name = ?, description = ?, price = ?, duration = ?, " +
            "last_update_date = ? where id = ?;";
    private static final String BY_ID = "where id = ?";
    private static final String BY_NAME = "where name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Certificate> rowMapper = BeanPropertyRowMapper.newInstance(Certificate.class);
    private final BeanPropertyRowMapper<Tag> tagMapper = BeanPropertyRowMapper.newInstance(Tag.class);

    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Certificate> findByName(String name) throws RepositoryException {
        try {
            String query = SELECT + BY_NAME;
            List<Certificate> certificates = jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, name);
                return statement;
            }, rowMapper);
            if (certificates.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(certificates.get(0));
            }
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Certificate> findAll() throws RepositoryException {
        try {
            List<Certificate> certificates = jdbcTemplate.query(connection ->
                    connection.prepareStatement(SELECT_ALL), rowMapper);
            return certificates;
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }

    public List<Certificate> findByTagName(String name) throws RepositoryException {
        try {
            List<Certificate> certificates = jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(SELECT_BY_TAG_NAME);
                statement.setString(1, name);
                return statement;
            }, rowMapper);
            return certificates;
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Certificate> findById(Long id) throws RepositoryException {
        try {
            String query = SELECT + BY_ID;
            List<Certificate> certificates = jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1,
                        id.toString());
                return statement;
            }, rowMapper);

            List<Tag> certificateTags = jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(SELECT_CERTIFICATE_TAGS);
                statement.setString(1, id.toString());
                return statement;
            }, tagMapper);
            Certificate certificate = certificates.get(0);
            certificate.setTags(certificateTags);
            return Optional.of(certificate);
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }

    private String getDateTime() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        String formattedDateTime = dateTime.format(formatter);
        return formattedDateTime.substring(0,23);
    }

    @Override
    public void save(Certificate entity) throws RepositoryException {
        try {
            String name = entity.getName();
            String description = entity.getDescription();
            BigDecimal price = entity.getPrice();
            Long duration = entity.getDuration();
            String time = getDateTime();
            System.out.println(time);
            //TODO add tags
//            List<Tag> tags = entity.getTags();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(SAVE_NEW);
                statement.setString(1, name);
                statement.setString(2, description);
                String priceParameter = price.toString();
                statement.setString(3, priceParameter);
                String durationParameter = duration.toString();
                statement.setString(4, durationParameter);
                statement.setString(5, time);
                statement.setString(6, time);
                return statement;
            });
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);
                statement.setString(1, id.toString());
                return statement;
            });
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(Certificate entity) throws RepositoryException {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(UPDATE);
                statement.setString(1,
                        entity.getName());
                statement.setString(2, entity.getDescription());
                statement.setString(3, entity.getPrice().toString());
                statement.setString(4, entity.getDuration().toString());
                statement.setString(5, String.valueOf(entity.getLastUpdateDate()));//lastUpdateDate;
                statement.setString(6,
                        entity.getId()
                                .toString());
                return statement;
            });
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }
}