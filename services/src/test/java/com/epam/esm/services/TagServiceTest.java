package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

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
        when(tagRepository.findAll()).thenReturn(TAGS);
        //when
        List<Tag> actual = tagService.getAll();
        //then
        Assertions.assertEquals(TAGS, actual);
    }

    @Test
    void testTagServiceShouldSaveNewTag() {
        //given
        //when
        tagService.save(FIRST_TAG);
        //then
        verify(tagRepository, times(wantedNumberOfInvocations)).save(FIRST_TAG);
    }

    @Test
    void testTagServiceShouldSaveNewTags() {
        //given
        //when
        tagService.save(TAGS);
        //then
        verify(tagRepository, times(wantedNumberOfInvocations)).save(TAGS);
    }

    @Test
    void testTagServiceShouldFindTagById() {
        //given
        Optional<Tag> expected = Optional.of(FIRST_TAG);
        Long tagId = FIRST_TAG.getId();
        when(tagRepository.findById(tagId)).thenReturn(expected);
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
        when(tagRepository.findByName(tagName)).thenReturn(expected);
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
        verify(tagRepository, times(wantedNumberOfInvocations)).delete(tagId);
    }
}