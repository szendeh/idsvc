/**
 * Copyright 2014 MIT Libraries
 * Licensed under: http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.mit.lib.idsvc;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.skife.jdbi.v2.DBI;

import edu.mit.lib.idsvc.db.ClaimDAO;
import edu.mit.lib.idsvc.db.IdentifierDAO;
import edu.mit.lib.idsvc.db.NameDAO;
import edu.mit.lib.idsvc.db.PersonDAO;
import edu.mit.lib.idsvc.db.WorkDAO;
import edu.mit.lib.idsvc.resources.ClaimResource;
import edu.mit.lib.idsvc.resources.NameResource;
import edu.mit.lib.idsvc.resources.PersonResource;
import edu.mit.lib.idsvc.resources.WorkResource;

public class IdentityServiceApplication extends Application<IdentityServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new IdentityServiceApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<IdentityServiceConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(IdentityServiceConfiguration configuration, Environment environment) throws ClassNotFoundException {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final ClaimDAO claimDao = jdbi.onDemand(ClaimDAO.class);
        final IdentifierDAO identifierDao = jdbi.onDemand(IdentifierDAO.class);
        final NameDAO nameDao = jdbi.onDemand(NameDAO.class);
        final WorkDAO workDao = jdbi.onDemand(WorkDAO.class);
        final PersonDAO personDao = jdbi.onDemand(PersonDAO.class);
        environment.jersey().register(new ClaimResource(claimDao, personDao, identifierDao, nameDao, workDao));
        environment.jersey().register(new NameResource(nameDao, personDao, claimDao));
        environment.jersey().register(new PersonResource(personDao, identifierDao, claimDao));
        environment.jersey().register(new WorkResource(workDao, personDao, claimDao));
    }
}