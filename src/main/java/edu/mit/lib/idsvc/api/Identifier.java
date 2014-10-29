/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a personal identifier - presumed to be unique
 * within its name-space/schema
 * 
 * @author richardrodgers
 */

public class Identifier {

    private int    id;
    private int    personId;
    private String schema;
    private String identifier;

    public Identifier(int id, int personId, String schema, String identifier) {
        this.id = id;
        this.personId = personId;
        this.schema = schema;
        this.identifier = identifier;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public int getPersonId() {
        return personId;
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