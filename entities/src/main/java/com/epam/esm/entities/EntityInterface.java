package com.epam.esm.entities;

import java.io.Serializable;

/**
 * The interface declares its successors must have an obligatory id field
 *
 * @author Anton Tomashevich
 * @version 1.0
 * @see Certificate
 * @see Tag
 */
public interface EntityInterface extends Serializable {

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
