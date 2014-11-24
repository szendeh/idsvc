/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a work - which is an intellectual
 * product authored by persons.
 * 
 * @author richardrodgers
 */

public class Work {

    private int id;

    public Work(int id) {
        this.id = id;
    }

    @JsonProperty
    public int getId() {
        return id;
    }
}