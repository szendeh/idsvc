/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc.api;

//import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of a claim which has been resolved
 * against the data model
 * 
 * @author richardrodgers
 */

public class ResolvedClaim {

    private int id;
    private String source;
    private int pidentId;
    private int widentId;
    private int pnameId;

    public ResolvedClaim() {}

    public ResolvedClaim(int id, String source, int pidentId, int widentId, int pnameId) {
        this.id = id;
        this.source = source;
        this.pidentId = pidentId;
        this.widentId = widentId;
        this.pnameId = pnameId;
    }

    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public int getPidentId() {
        return pidentId;
    }

    public int getWidentId() {
        return widentId;
    }

    public int getPnameId() {
        return pnameId;
    }
}
