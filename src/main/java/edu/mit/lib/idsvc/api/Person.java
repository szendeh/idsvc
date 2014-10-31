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
    private String label;

    public Person() {}

    public Person(int id) {
        this.id = id;
    }

    public Person(int id, String label) {
        this.id = id;
        this.label = label;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty
    public String getLabel() {
        return label;
    }

    @JsonProperty
    public void setLabel(String label) {
        this.label = label;
    }
}
