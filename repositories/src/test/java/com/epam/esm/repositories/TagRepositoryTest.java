package com.epam.esm.repositories;

import com.epam.esm.entities.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class TagRepositoryTest {

    private static final Tag FIRST_TAG = new Tag(1L, "Test tag 1");
    private static final Tag SECOND_TAG = new Tag(2L, "Test tag 2");
    private static final Tag THIRD_TAG = new Tag(3L, "Test tag 3");
    private static final Tag FOURTH_TAG = new Tag(4L, "Test tag 4");
    private static final Tag ADDITIONAL_TAG = new Tag(5L, "Additional tag 1");
    private static final List<Tag> TAGS = Arrays.asList(FIRST_TAG, SECOND_TAG, THIRD_TAG, FOURTH_TAG);

    private TagRepository tagRepository;

    @BeforeEach
    public void createDb() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("scripts/init_test.sql")
                .addScript("scripts/init_test_data.sql")
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        tagRepository = new TagRepository(jdbcTemplate);
    }

    @Test
    void testTagRepositoryShouldFindTagByName() {
        //given
        Optional<Tag> expected = Optional.of(FIRST_TAG);
        String tagName = FIRST_TAG.getName();
        //when
        Optional<Tag> actual = tagRepository.findByName(tagName);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTagRepositoryShouldGetAllTagsFromDatabase() {
        //given
        //when
        List<Tag> actual = tagRepository.findAll();
        //then
        Assertions.assertEquals(TAGS, actual);
    }

    @Test
    void testTagRepositoryShouldFindTagById() {
        //given
        Optional<Tag> expected = Optional.of(SECOND_TAG);
        Long tagId = SECOND_TAG.getId();
        //when
        Optional<Tag> actual = tagRepository.findById(tagId);
        //when
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTagRepositoryShouldAddEntryToDatabase() {
        //given
        tagRepository.save(ADDITIONAL_TAG);
        Long tagId = ADDITIONAL_TAG.getId();
        Optional<Tag> expected = Optional.of(ADDITIONAL_TAG);
        //when
        Optional<Tag> actual = tagRepository.findById(tagId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTagRepositoryShouldDeleteEntryByTagId() {
        //given
        Long tagId = FOURTH_TAG.getId();
        tagRepository.delete(tagId);
        Optional<Tag> expected = Optional.empty();
        //when
        Optional<Tag> actual = tagRepository.findById(tagId);
        //then
        Assertions.assertEquals(expected, actual);
    }
}