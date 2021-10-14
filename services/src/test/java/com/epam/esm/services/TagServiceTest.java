package com.epam.esm.services;

import com.epam.esm.entities.Tag;
import com.epam.esm.repositories.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private static final Tag FIRST_TAG = new Tag(1L, "First test tag");
    private static final Tag SECOND_TAG = new Tag(2L, "Second test tag");
    private static final List<Tag> TAGS = Arrays.asList(FIRST_TAG, SECOND_TAG);

    private final int wantedNumberOfInvocations = 1;

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagService tagService;

    @Test
    void testTagServiceShouldReturnMultiplyEntitiesWhenGetAllCalled() {
        //given
        Mockito.when(tagRepository.findAll(0,2)).thenReturn(TAGS);
        //when
        List<Tag> actual = tagService.getAll(1,2);
        //then
        Assertions.assertEquals(TAGS, actual);
    }

    @Test
    void testTagServiceShouldSaveNewTag() {
        //given
        //when
        tagService.save(FIRST_TAG);
        //then
        Mockito.verify(tagRepository, Mockito.times(wantedNumberOfInvocations)).save(FIRST_TAG);
    }

    @Test
    void testTagServiceShouldFindTagById() {
        //given
        Optional<Tag> expected = Optional.of(FIRST_TAG);
        Long tagId = FIRST_TAG.getId();
        Mockito.when(tagRepository.findById(tagId)).thenReturn(expected);
        //when
        Optional<Tag> actual = tagService.get(tagId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTagServiceShouldFindTagByName() {
        //given
        Optional<Tag> expected = Optional.of(FIRST_TAG);
        String tagName = FIRST_TAG.getName();
        Mockito.when(tagRepository.findByName(tagName)).thenReturn(expected);
        //when
        Optional<Tag> actual = tagService.get(tagName);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testTagServiceShouldDeleteTagById() {
        //given
        Long tagId = FIRST_TAG.getId();
        //when
        tagService.delete(tagId);
        //then
        Mockito.verify(tagRepository, Mockito.times(wantedNumberOfInvocations)).delete(tagId);
    }
}