package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository implements CrudRepositoryInterface<Tag>  {

    private static final Logger LOGGER = Logger.getLogger(TagRepository.class);

    private static final String SELECT_ALL = "select * from tag";
    private static final String SELECT_BY_ID = "select * from tag where id = ?";
    private static final String SELECT_BY_NAME = "select * from tag where name = ?";
    private static final String SAVE = "insert into tag set name = ?";
    private static final String DELETE_BY_ID = "delete from tag where id = ?";
    private static final String UPDATE = "update tag set name = ? where id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Tag> rowMapper = BeanPropertyRowMapper.newInstance(Tag.class);

    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Tag> findByName(String name) throws RepositoryException {
        try {
            List<Tag> tagList = jdbcTemplate.query(con -> {
                PreparedStatement statement = con.prepareStatement(SELECT_BY_NAME);
                statement.setString(1, name);
                return statement;
            }, rowMapper);
            return Optional.ofNullable(
                    tagList.get(0));
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Tag> findAll() throws RepositoryException {
        try {
            List<Tag> tags = jdbcTemplate.query(connection ->
                    connection.prepareStatement(SELECT_ALL), rowMapper);
            return tags;
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }

    }

    @Override
    public Optional<Tag> findById(Long id) throws RepositoryException {
        try {
            List<Tag> tagList = jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);
                statement.setString(1,
                        id.toString());
                return statement;
            }, rowMapper);
            Tag tag = tagList.get(0);
            return Optional.of(tag);
        } catch (DataAccessException e) {
            LOGGER.error(e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public void save(Tag entity) throws RepositoryException {
        try {
            String name = entity.getName();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(SAVE);
                statement.setString(1, name);
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
    public void update(Tag entity) throws RepositoryException {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(UPDATE);
                statement.setString(1,
                        entity.getName());
                statement.setString(2,
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