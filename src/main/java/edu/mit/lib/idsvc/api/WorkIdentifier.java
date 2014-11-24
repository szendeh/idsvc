/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a work identifier - presumed to be unique
 * within its name-space/schema
 * 
 * @author richardrodgers, szendeh
 */

public class WorkIdentifier {

    private int    id;
    private int    workId;
    private String schema;
    private String identifier;

    public WorkIdentifier(int id, int workId, String schema, String identifier) {
        this.id = id;
        this.workId = workId;
        this.schema = schema;
        this.identifier = identifier;
    }

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonProperty
    public int getWorkId() {
        return workId;
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