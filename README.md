## Identity Data Web Service ##

The service maintains a model of author attributions to works.

### Installing and running the service ###

1. Prerequisites: Java7 SDK, Git, Maven (build), PostgreSQL (run)

2. Download code from GitHub and build

    git clone https://github.com/richardrodgers/idsvc.git

    mvn clean package

3. Database Setup

    edit idsvc.yml so that database user, password, and jdbc URL are correct credentials for your postgresql server

    create an 'idsvc' database in postgres server and run the DDL script 'postgres.sql' to initialize it

4. Run the service

    java -jar target/isvc-api-0.1-SNAPSHOT.jar server idsvc.yml

Service should be up and running on port 8080 (the jar includes an embedded Jetty for the web server).

### Service API ###

The service produces JSON output and expects JSON input.

#### Updates ####

The service exposes only one resource type ('claim') for updating the model.
A PUT to the URL (example data):
    
    http://example.com:8080/claim/900058367?wid=1721.1/69157

will 'make' or affirm a claim, whereas a DELETE to the same URL will 'retract' the claim.
A claim PUT must contain as the resource representation, a JSON object like this:

    {
        "work": "1721.1/69157",
        "identifier": "900058367",
        "name": "Richard Rodgers"
    }

A curl invocation of this (where above JSON is in 'claim.js'):

    curl -X PUT -H 'Content-Type: application/json' --data-binary @claim.js http://example.com/claim/900058367?wid=1721.1/69157

Affirming a claim simply means that the occurance of the name literal in the context of the given work (CNRI handle assumed for now) will
be understood to refer to the person uniquely identified by the given identifier (MIT ID schema assumed for now).

#### Access ####

Access to the model is performed via GET requests to 3 entity types: 'person', 'work' and 'name'.
Each type my be requested *either* by ID (i.e. db key), or by a _reference_. For example, the URL:

    http://example.com:8080/person/1

will return the first person created in the model. Alternatively:

    http://example.com/8080/person/ref/mitid?ref=900058367

will return the person (if he exists in the model) whose MIT ID is the ref parameter. The *mitid*
path parameter is known as the 'schema' (name-space) for the identifer. Currently, 'mitid' is
hard-coded as the only schema known to the model.

Similarly for works:

    http://example.com:8080/work/1

will return the first work created in the model. Alternatively:

    http://example.com:8080/work/ref/cnri?ref=1721.1/69157

will return the work (if it exists in the model) whose handle ('cnri' is the schema name for handles) is
the ref parameter.

The 'name' entity is *slightly* different. The ID URL:

    http://example.com:8080/name/1

is the same, but because name literals don't fall into a controlled name-space, the 'schema' path parameter
is replaced with the string 'literal':

    http://example.com:8080/name/literal?ref=Richard+Rodgers

Note that names have to be URL-encoded, since they typically contain spaces.

### Representations ###

For persons, works and names, the returned resource representation is actually a sub-graph of the model
maintained by the service. For persons, the sub-graph contains all works authored by person, all names
the person has been claimed under, and all personal identifiers associated with the person. For works, the 
sub-graph contains all authors (persons) and all names associated with the work. Finally for names, the
sub-graph contains all works the names appear in and all persons who have that name.




