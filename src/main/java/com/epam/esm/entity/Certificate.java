package com.epam.esm.entities;

import java.math.BigDecimal;

public class Certificate implements Entity {

    private Long id;
    private Long tagId;
    private String name;
    private String description;
    private BigDecimal price;
    private Long duration;
    private String createDate; // ISO 8601 format
    private String lastUpdateDate; // ISO 8601 format - ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT )



    @Override
    public Long getId() {
        return id;
    }
}
