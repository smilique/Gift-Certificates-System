package com.epam.esm.entity;

/**
 * The interface declares its successors must have an obligatory id field
 *
 * @author Anton Tomashevich
 * @version 1.0
 * @see com.epam.esm.entity.Certificate
 * @see com.epam.esm.entity.Tag
 */
public interface Entity {

    /**
     * The method sets a Long identifier of object
     */
    void setId(Long id);

    /**
     * The method sets a Long identifier of object
     * @return Long
     */
    Long getId();
}
