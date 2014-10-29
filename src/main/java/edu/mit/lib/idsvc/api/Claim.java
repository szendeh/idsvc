/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a claim - which is the assignment of a unique
 * identity as referent to a name in a work
 * 
 * @author richardrodgers
 */

public class Claim {

    private String work;
    private String identifier;
    private String name;

    public Claim() {}

    public Claim(String work, String identifier, String name) {
        this.work = work;
        this.identifier = identifier;
        this.name = name;
    }

    @JsonProperty
    public String getWork() {
        return work;
    }

    @JsonProperty
    public void setWork(String work) {
        this.work = work;
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
}
