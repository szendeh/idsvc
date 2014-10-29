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
    private String schema;
    private String identifier;

    public Work(int id, String schema, String identifier) {
        this.id = id;
        this.schema = schema;
        this.identifier = identifier;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public String getSchema() {
        return schema;
    }

    @JsonProperty
    public String getIdentifier() {
        return identifier;
    }
}