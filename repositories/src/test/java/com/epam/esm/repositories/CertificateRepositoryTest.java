package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class CertificateRepositoryTest {

    private static final Certificate FIRST_CERTIFICATE = new Certificate(1L, "B# Test certificate 1",
            "Certificate 1 description",
            BigDecimal.valueOf(1300.00), 30L,
            ZonedDateTime.parse("2018-08-29T06:12:15.100+03:00"),
            ZonedDateTime.parse("2018-08-29T06:12:15.100+03:00"));
    private static final Certificate SECOND_CERTIFICATE = new Certificate(2L, "A# Test certificate 2",
            "Certificate 2 description",
            BigDecimal.valueOf(2000.00), 90L,
            ZonedDateTime.parse("2018-10-29T06:12:15.200+03:00"),
            ZonedDateTime.parse("2018-10-29T06:12:15.200+03:00"));
    private static final Certificate THIRD_CERTIFICATE = new Certificate(3L, "C Test certificate 3",
            "Certificate 3 description",
            BigDecimal.valueOf(1000.00), 90L,
            ZonedDateTime.parse("2018-09-29T06:12:15.200+03:00"),
            ZonedDateTime.parse("2018-09-29T06:12:15.200+03:00"));
    private static final Certificate ADDITIONAL_CERTIFICATE = new Certificate(4L, "Additional",
            "Additional certificate description",
            BigDecimal.valueOf(1000.00), 90L,
            ZonedDateTime.parse("2018-09-29T06:12:15.200+03:00"),
            ZonedDateTime.parse("2018-09-29T06:12:15.200+03:00"));
    private static final Certificate CHANGED_CERTIFICATE = new Certificate(5L, "Change it",
            "Certificate description",
            BigDecimal.valueOf(1000.00), 90L,
            ZonedDateTime.parse("2018-09-29T06:12:15.200+03:00"),
            ZonedDateTime.parse("2018-09-29T06:12:15.200+03:00"));
    private static final Tag FIRST_TAG = new Tag(1L, "Test tag 1");
    private static final Tag SECOND_TAG = new Tag(2L, "Test tag 2");
    private static final Tag THIRD_TAG = new Tag(3L, "Test tag 3");
    private static final Tag FOURTH_TAG = new Tag(4L, "Test tag 4");
    private static final List<Certificate> CERTIFICATES = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE);
    private static final String DATE_SORT = "desc";
    private static final String NAME_SORT = "asc";
    private static final String SEARCH_STRING = "#";
    private static final String UPDATED_NAME = "Changed";
    private static final Tag[] EMPTY_TAGS = new Tag[]{};

    private CertificateRepository certificateRepository;

    @BeforeEach
    public void createDb() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("scripts/init_test.sql")
                .addScript("scripts/init_test_data.sql")
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        certificateRepository = new CertificateRepository(jdbcTemplate);
    }

    @Test
    void testCertificateRepositoryShouldFindAllCertificateEntitiesFromDb() {
        //given
        FIRST_CERTIFICATE.setTags(FIRST_TAG, FOURTH_TAG);
        SECOND_CERTIFICATE.setTags(SECOND_TAG, FOURTH_TAG, THIRD_TAG);
        THIRD_CERTIFICATE.setTags(THIRD_TAG, FIRST_TAG);
        //when
        List<Certificate> actual = certificateRepository.findAll();
        //then
        Assertions.assertArrayEquals(CERTIFICATES.toArray(), actual.toArray());
    }

    @Test
    void testCertificateRepositoryShouldFindCertificateEntitiesById() {
        //given
        Long certificateId = SECOND_CERTIFICATE.getId();
        Optional<Certificate> expected = Optional.of(SECOND_CERTIFICATE);
        //when
        Optional<Certificate> actual = certificateRepository.findById(certificateId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCertificateRepositoryShouldFindEntitiesByCertificateName() {
        //given
        String certificateName = THIRD_CERTIFICATE.getName();
        Optional<Certificate> expected = Optional.of(THIRD_CERTIFICATE);
        //when
        Optional<Certificate> actual = certificateRepository.findByName(certificateName);
        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void testCertificateRepositoryShouldFindCertificatesByTagName() {
        //given
        String tagName = THIRD_TAG.getName();
        List<Certificate> expected = Arrays.asList(SECOND_CERTIFICATE, THIRD_CERTIFICATE);
        //when
        List<Certificate> actual = certificateRepository.findByTagName(tagName);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCertificateRepositoryShouldSortFoundResults() {
        //given
        FIRST_CERTIFICATE.setTags(FIRST_TAG, FOURTH_TAG);
        SECOND_CERTIFICATE.setTags(SECOND_TAG, FOURTH_TAG, THIRD_TAG);
        THIRD_CERTIFICATE.setTags(THIRD_TAG, FIRST_TAG);
        List<Certificate> expected = Arrays.asList(SECOND_CERTIFICATE, FIRST_CERTIFICATE, THIRD_CERTIFICATE);
        //when
        List<Certificate> actual = certificateRepository.findAll(NAME_SORT, DATE_SORT);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAndSortShouldFindEntitiesByPartOfCertificateNameAndSortResults() {
        //given
        FIRST_CERTIFICATE.setTags(FIRST_TAG, FOURTH_TAG);
        SECOND_CERTIFICATE.setTags(SECOND_TAG, FOURTH_TAG, THIRD_TAG);
        List<Certificate> expected = Arrays.asList(SECOND_CERTIFICATE, FIRST_CERTIFICATE);
        //when
        List<Certificate> actual = certificateRepository.findAndSort(NAME_SORT, DATE_SORT, SEARCH_STRING);
        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void testCertificateRepositoryShouldFindEntitiesByPartOfCertificateName() {
        //given
        FIRST_CERTIFICATE.setTags(FIRST_TAG, FOURTH_TAG);
        SECOND_CERTIFICATE.setTags(SECOND_TAG, FOURTH_TAG, THIRD_TAG);
        List<Certificate> expected = Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE);
        //when
        List<Certificate> actual = certificateRepository.find(SEARCH_STRING);
        //then
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void testCertificateRepositoryShouldSaveNewCertificateEntity() {
        //given
        Optional<Certificate> expected = Optional.of(ADDITIONAL_CERTIFICATE);
        certificateRepository.save(ADDITIONAL_CERTIFICATE);
        Long certificateId = ADDITIONAL_CERTIFICATE.getId();
        ADDITIONAL_CERTIFICATE.setTags(EMPTY_TAGS);
        //when
        Optional<Certificate> actual = certificateRepository.findById(certificateId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCertificateRepositoryShouldDeleteEntity() {
        //given
        Long certificateId = FIRST_CERTIFICATE.getId();
        certificateRepository.delete(certificateId);
        Optional<Certificate> expected = Optional.empty();
        //when
        Optional<Certificate> actual = certificateRepository.findById(certificateId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCertificateRepositoryShouldUpdateEntityFields() {
        //given
        certificateRepository.save(CHANGED_CERTIFICATE);
        CHANGED_CERTIFICATE.setName(UPDATED_NAME);
        Optional<Certificate> expected = Optional.of(CHANGED_CERTIFICATE);
        certificateRepository.save(CHANGED_CERTIFICATE);
        CHANGED_CERTIFICATE.setTags(EMPTY_TAGS);
        //when
        Optional<Certificate> actual = certificateRepository.findByName(UPDATED_NAME);
        //then
        Assertions.assertEquals(expected, actual);
    }
}