package com.epam.esm.services;

import com.epam.esm.entities.Certificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repositories.CertificateRepository;
import com.epam.esm.repositories.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CertificateServiceTest {

    private static final Certificate FIRST_CERTIFICATE = new Certificate(1L, "B# Test certificate 1",
            "Certificate 3 description",
            BigDecimal.valueOf(1300), 30L,
            ZonedDateTime.parse("2018-08-29T06:12:15.100Z"),
            ZonedDateTime.parse("2018-08-29T06:12:15.100Z"));
    private static final Certificate SECOND_CERTIFICATE = new Certificate(2L, "A# Test certificate 2",
            "Certificate 3 description",
            BigDecimal.valueOf(2000), 90L,
            ZonedDateTime.parse("2018-10-29T06:12:15.200Z"),
            ZonedDateTime.parse("2018-10-29T06:12:15.200Z"));
    private static final Certificate THIRD_CERTIFICATE = new Certificate(3L, "C Test certificate 3",
            "Certificate 3 description",
            BigDecimal.valueOf(1000), 90L,
            ZonedDateTime.parse("2018-09-29T06:12:15.200Z"),
            ZonedDateTime.parse("2018-09-29T06:12:15.200Z"));
    private static final List<Certificate> CERTIFICATES = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE);
    private static final Tag TAG = new Tag(1L, "TestTag");
    private static final String DATE_SORT = "desc";
    private static final String NAME_SORT = "asc";
    private static final String SEARCH_STRING = "#";
    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;

    @Mock
    CertificateRepository certificateRepository;

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    CertificateService certificateService;

    @Test
    void testCertificateServiceShouldReturnCertificateEntitiesFromDb() {
        //given
        Mockito.when(certificateRepository.findAll()).thenReturn(CERTIFICATES);
        //when
        List<Certificate> actual = certificateService.getAll();
        //then
        Assertions.assertEquals(CERTIFICATES, actual);
    }

    @Test
    void testCertificateServiceShouldSearchCertificateEntitiesByString() {
        //given
        List<Certificate> expected = new ArrayList<>();
        expected.add(THIRD_CERTIFICATE);
        String searchString = THIRD_CERTIFICATE.getDescription();
        Mockito.when(certificateRepository.find(searchString)).thenReturn(expected);
        //when
        List<Certificate> actual = certificateService.getByName(searchString);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCertificateServiceShouldSortResultsByCertificateNameAndDate() {
        //given
        List<Certificate> expected = Arrays.asList(SECOND_CERTIFICATE, FIRST_CERTIFICATE, THIRD_CERTIFICATE);
        Mockito.when(certificateRepository.findAll(NAME_SORT, DATE_SORT)).thenReturn(expected);
        //when
        List<Certificate> actual = certificateService.getByName(NAME_SORT, DATE_SORT);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCertificateServiceShouldSortSearchResultsByCertificateNameAndDate() {
        //given
        List<Certificate> expected = Arrays.asList(SECOND_CERTIFICATE, FIRST_CERTIFICATE);
        Mockito.when(certificateRepository.findAndSort(NAME_SORT, DATE_SORT, SEARCH_STRING)).thenReturn(expected);
        //when
        List<Certificate> actual = certificateService.search(NAME_SORT, DATE_SORT, SEARCH_STRING);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCertificateServiceShouldPassCertificateEntityToRepository() {
        //given
        List<Tag> tags = Arrays.asList(TAG);
        FIRST_CERTIFICATE.setTags(tags);
        Long tagId = TAG.getId();
        Mockito.when(tagRepository.findByName(FIRST_CERTIFICATE.getName())).thenReturn(Optional.of(TAG));
        Mockito.when(tagRepository.saveNew(TAG)).thenReturn(tagId);
        Mockito.when(certificateRepository.findById(tagId)).thenReturn(Optional.of(FIRST_CERTIFICATE));
        //when
        certificateService.save(FIRST_CERTIFICATE);
        //then
        Mockito.verify(certificateRepository, Mockito.times(WANTED_NUMBER_OF_INVOCATIONS)).createNewCertificate(FIRST_CERTIFICATE, tags);
    }

    @Test
    void testCertificateServiceShouldFindCertificateEntityByItsId() {
        //given
        Long certificateId = FIRST_CERTIFICATE.getId();
        Optional<Certificate> expected = Optional.of(FIRST_CERTIFICATE);
        Mockito.when(certificateRepository.findById(certificateId)).thenReturn(expected);
        //when
        Optional<Certificate> actual = certificateService.get(certificateId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCertificateServiceShouldPassUpdatedCertificateEntityToRepository() {
        //given
        //when
        certificateService.update(FIRST_CERTIFICATE);
        //then
        Mockito.verify(certificateRepository, Mockito.times(WANTED_NUMBER_OF_INVOCATIONS)).update(FIRST_CERTIFICATE);
    }

    @Test
    void testCertificateServiceShouldDeleteEntityById() {
        //given
        Long certificateId = FIRST_CERTIFICATE.getId();
        //when
        certificateService.delete(certificateId);
        //then
        Mockito.verify(certificateRepository, Mockito.times(WANTED_NUMBER_OF_INVOCATIONS)).delete(certificateId);
    }
}