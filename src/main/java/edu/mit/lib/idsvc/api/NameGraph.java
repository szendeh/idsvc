/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a personal name - which is *not*
 * presumed to be unique within the universe of persons,
 * the works it appears in, and the persons it is attributed to.
 * 
 * @author richardrodgers
 */

public class NameGraph {

    private Name name;
    private List<Person> persons;
    private List<Work> works;

    public NameGraph(Name name, List<Person> persons, List<Work> works) {
        this.name = name;
        this.persons = persons;
        this.works = works;
    }

    @JsonProperty
    public Name getName() {
        return name;
    }

    @JsonProperty
    public List<Person> getPersons() {
        return persons;
    }

    @JsonProperty
    public List<Work> getWorks() {
        return works;
    }
}
