/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a Person - which is a unique identity,
 * and her works, names, and identities.
 * 
 * @author richardrodgers
 */

public class PersonGraph {

    private Person person;
    private List<Name> names;
    private List<Identifier> identifiers;
    private List<Work> works;

    public PersonGraph(Person person, List<Name> names, List<Identifier> identifiers, List<Work> works) {
        this.person = person;
        this.names = names;
        this.identifiers = identifiers;
        this.works = works;
    }

    @JsonProperty
    public Person getPerson() {
        return person;
    }

    @JsonProperty
    public List<Name> getNames() {
        return names;
    }

    @JsonProperty
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    @JsonProperty
    public List<Work> getWorks() {
        return works;
    }
}