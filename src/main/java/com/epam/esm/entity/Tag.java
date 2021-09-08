package com.epam.esm.entities;

public class Tag implements Entity {

    private Long id;
    private String name;

    @Override
    public Long getId() {
        return id;
    }
}
