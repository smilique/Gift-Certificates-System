package com.epam.esm.entities;

/**
 * The interface declares it's successors must have a returnable id value
 *
 * @author Anton Tomashevich
 */

public interface Entity {

    /**
     * The method returns an identifier of object
     *
     * @return Long - unique identifier of instance
     */

    Long getId();
}
