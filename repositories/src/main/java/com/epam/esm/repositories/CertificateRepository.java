package com.epam.esm.repositories;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CertificateRepository implements CrudRepositoryInterface<Certificate> {

    private static final String SELECT_ALL = "select * from gift_certificate";
    private static final String SELECT_BY_TAG_NAME = "select gc.id, gc.name, gc.description, gc.price, gc.duration, " +
            "gc.create_date, gc.last_update_date from gift_certificate gc " +
            "join tagged_gift_certificate tgc on gc.id = tgc.gift_certificate_id " +
            "join tag on tag.id = tgc.tag_id where tag.name = ?";
    private static final String SELECT = "select * from gift_certificate ";
    private static final String SELECT_LIKE = "select * from gift_certificate where name like ? or description like ?";
    private static final String SELECT_CERTIFICATE_TAGS = "select tag.id, tag.name from tag " +
            "join tagged_gift_certificate tgc on tag.id = tgc.tag_id where tgc.gift_certificate_id = ?";
    private static final String DELETE_BY_ID = "delete from gift_certificate where id = ?";
    private static final String SAVE_NEW = "insert into gift_certificate set name = ?, description = ?, price = ?, duration = ?, " +
            "create_date = ?, last_update_date = ? ";
    private static final String UPDATE = "update gift_certificate set name = ?, description = ?, price = ?, duration = ?, " +
            "last_update_date = ? where id = ?;";
    private static final String BY_ID = "where id = ?";
    private static final String BY_NAME = "where name = ?";
    private static final String INSERT_TAGGED_CERTIFICATE = "insert into tagged_gift_certificate (gift_certificate_id, tag_id) values ";
    private static final String VALUE_BLOCK_START = "(";
    private static final String VALUE_BLOCK_HAS_NEXT = "), ";
    private static final String VALUE_DELIMITER = ", ";
    private static final String VALUE_BLOCK_END = ")";
    private static final String ORDER_BY_NAME = " order by name ";
    private static final String ORDER_BY_DATE = ", create_date ";
    private static final String TAG_ID_GENERATED_KEY = "GENERATED_KEY";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Certificate> rowMapper = BeanPropertyRowMapper.newInstance(Certificate.class);
    private final BeanPropertyRowMapper<Tag> tagMapper = BeanPropertyRowMapper.newInstance(Tag.class);

    public CertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Certificate> findAll() {
        List<Certificate> certificates = jdbcTemplate.query(connection ->
                connection.prepareStatement(SELECT_ALL), rowMapper);
        return addTags(certificates);
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        String query = SELECT + BY_ID;
        List<Certificate> certificates = jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,
                    id.toString());
            return statement;
        }, rowMapper);
        if (certificates.isEmpty()) {
            return Optional.empty();
        } else {
            Certificate certificate = certificates.get(0);
            return addTags(certificate);
        }
    }

    public Optional<Certificate> findByName(String name) {
        String query = SELECT + BY_NAME;
        List<Certificate> certificates = jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            return statement;
        }, rowMapper);
        if (certificates.isEmpty()) {
            return Optional.empty();
        } else {
            Certificate certificate = certificates.get(0);
            return addTags(certificate);
        }
    }

    public List<Certificate> findByTagName(String name) {
        List<Certificate> certificates = jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_TAG_NAME);
            statement.setString(1, name);
            return statement;
        }, rowMapper);
        return addTags(certificates);
    }

    public List<Certificate> findAll(String nameSort, String dateSort) {
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT_ALL);
        builder.append(ORDER_BY_NAME);
        builder.append(nameSort);
        builder.append(ORDER_BY_DATE);
        builder.append(dateSort);
        String query = builder.toString();
        List<Certificate> certificates = jdbcTemplate.query(connection ->
                connection.prepareStatement(query), rowMapper);
        return addTags(certificates);
    }

    public List<Certificate> findAndSort(String nameSort, String dateSort, String searchString) {
        StringBuilder builder = new StringBuilder();
        builder.append(SELECT_LIKE);
        builder.append(ORDER_BY_NAME);
        builder.append(nameSort);
        builder.append(ORDER_BY_DATE);
        builder.append(dateSort);
        String query = builder.toString();
        List<Certificate> certificates = searchForValue(searchString, query);
        return addTags(certificates);
    }

    public List<Certificate> find(String searchString) {
        List<Certificate> certificates = searchForValue(searchString, SELECT_LIKE);
        return addTags(certificates);
    }

    public void setTagsToCertificates(List<Map<String, Object>> tagKeys, Long certificateId) {
        StringBuilder builder = new StringBuilder();
        builder.append(INSERT_TAGGED_CERTIFICATE);
        for (int i = 0; i < tagKeys.size(); i++) {
            Map<String, Object> keyMap = tagKeys.get(i);
            builder.append(VALUE_BLOCK_START);
            builder.append(certificateId);
            builder.append(VALUE_DELIMITER);
            builder.append(keyMap
                    .get(TAG_ID_GENERATED_KEY));
            if (i + 1 == tagKeys.size()) {
                builder.append(VALUE_BLOCK_END);
            } else {
                builder.append(VALUE_BLOCK_HAS_NEXT);
            }
        }
        String query = builder.toString();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement;
        });
    }

    @Override
    public void save(Certificate entity) {
        String name = entity.getName();
        String description = entity.getDescription();
        BigDecimal price = entity.getPrice();
        Long duration = entity.getDuration();
        String time = getDateTime();
        List<Tag> tags = entity.getTags();
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
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);
            statement.setString(1,
                    id.toString());
            return statement;
        });
    }

    @Override
    public void update(Certificate entity) {
        List<Tag> tags = entity.getTags();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1,
                    entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setString(3, entity.getPrice()
                    .toString());
            statement.setString(4, entity.getDuration()
                    .toString());
            statement.setString(5, getDateTime());
            statement.setString(6,
                    entity.getId()
                            .toString());
            return statement;
        });
    }

    private List<Certificate> searchForValue(String searchString, String query) {
        String search = "%" + searchString + "%";
        List<Certificate> certificates = jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, search);
            statement.setString(2, search);
            return statement;
        }, rowMapper);
        return addTags(certificates);
    }

    private List<Certificate> addTags(List<Certificate> certificates) {
        for (Certificate certificate : certificates) {
            setTags(certificate);
        }
        return certificates;
    }

    private Optional<Certificate> addTags(Certificate certificate) {
        setTags(certificate);
        return Optional.of(certificate);
    }

    private void setTags(Certificate certificate) {
        List<Tag> certificateTags = jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(SELECT_CERTIFICATE_TAGS);
            Long id = certificate.getId();
            statement.setString(1,
                    id.toString());
            return statement;
        }, tagMapper);
        certificate.setTags(certificateTags);
    }

    private String getDateTime() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        String formattedDateTime = dateTime.format(formatter);
        return formattedDateTime.substring(0, 23);
    }
}