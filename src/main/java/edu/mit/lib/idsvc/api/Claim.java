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
    private String workIdentifier;
    private String workSchema;

    public Claim() {}

    public Claim(String identifier, String name, String source, String workIdentifier, String workSchema) {
        this.identifier = identifier;
        this.name = name;
        this.source = source;
        this.workIdentifier = workIdentifier;
        this.workSchema = workSchema;
    }

    @JsonProperty
    public String getWorkIdentifier() {
        return workIdentifier;
    }

    @JsonProperty
    public void setWorkIdentifier(String workIdentifier) {
        this.workIdentifier = workIdentifier;
    }

    @JsonProperty
    public String getWorkSchema() {
        return workSchema;
    }

    @JsonProperty
    public void setWorkSchema(String workSchema) {
        this.workSchema = workSchema;
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
