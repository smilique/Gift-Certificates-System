package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TagRepository implements CrudRepositoryInterface<Tag> {

    private static final String SELECT_ALL = "select * from tag";
    private static final String SELECT_BY_ID = "select * from tag where id = ?";
    private static final String SELECT_BY_NAME = "select * from tag where name = ?";
    private static final String SAVE = "insert into tag set name = ?";
    private static final String DELETE_BY_ID = "delete from tag where id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Tag> rowMapper = BeanPropertyRowMapper.newInstance(Tag.class);

    public TagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Tag> findByName(String name) {
        List<Tag> tagList = jdbcTemplate.query(con -> {
            PreparedStatement statement = con.prepareStatement(SELECT_BY_NAME);
            statement.setString(1, name);
            return statement;
        }, rowMapper);
        return Optional.ofNullable(
                tagList.get(0));
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> tags = jdbcTemplate.query(connection ->
                connection.prepareStatement(SELECT_ALL), rowMapper);
        return tags;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        List<Tag> tagList = jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID);
            statement.setString(1,
                    id.toString());
            return statement;
        }, rowMapper);
        if (tagList.size() > 0) {
            Tag tag = tagList.get(0);
            return Optional.of(tag);
        } else {
            return Optional.empty();
        }
    }

    public List<Map<String, Object>> save(List<Tag> tags) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        StringBuilder builder = new StringBuilder();
        builder.append("insert into tag (name) values");
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            builder.append("('");
            builder.append(
                    tag.getName());
            if (i + 1 == tags.size()) {
                builder.append("')");
            } else {
                builder.append("'), ");
            }
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    builder.toString(),
                    Statement.RETURN_GENERATED_KEYS);
            return statement;
        }, keyHolder);
        return keyHolder.getKeyList();
    }

    @Override
    public void save(Tag entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String name = entity.getName();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SAVE);
            statement.setString(1, name);
            return statement;
        }, keyHolder);
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
    public void update(Tag entity) {
    }
}