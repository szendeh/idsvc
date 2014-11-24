/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a claim - which is the assignment of a unique
 * identity as referent to a name in a work
 * 
 * @author richardrodgers
 */

public class Claim {
    private String identifier;
    private String name;
    private String source;
    private String work_identifier;
    private String work_schema;

    public Claim() {}

    public Claim(String identifier, String name, String source, String work_identifier, String work_schema) {
        this.identifier = identifier;
        this.name = name;
        this.source = source;
        this.work_identifier = work_identifier;
        this.work_schema = work_schema;
    }

    @JsonProperty
    public String getWork_identifier() {
        return work_identifier;
    }

    @JsonProperty
    public void setWork_identifier(String work_identifier) {
        this.work_identifier = work_identifier;
    }

    @JsonProperty
    public String getWork_schema() {
        return work_schema;
    }

    @JsonProperty
    public void setWork_schema(String work_schema) {
        this.work_schema = work_schema;
    }

    @JsonProperty
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getSource() {
        return source;
    }

    @JsonProperty
    public void setSource(String source) {
        this.source = source;
    }
}
