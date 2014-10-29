/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a Person - which is a unique identity
 * 
 * @author richardrodgers
 */

public class Person {

    private int id;

    public Person(int id) {
        this.id = id;
    }

    @JsonProperty
    public int getId() {
        return id;
    }
}
