/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a personal name - which is *not*
 * presumed to be unique within the universe of persons.
 * 
 * @author richardrodgers
 */

public class Name {

    private int id;
    private String name;

    public Name(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }
}