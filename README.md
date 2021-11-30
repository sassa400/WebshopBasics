# Webshop Basics

### Description
A simple REST service that can be used as a webshop backend.

It uses PostgreSQL DB, but you can easily configure it for any other DB. DDL is defined in `schema.sql`.

For convenience, Swagger is included for API testing. Also, WireMock is used for mocking external API calls.

### Setting up the local environment

#### Database

PostgreSQL could be installed in Docker, see [postgres Docker image](https://hub.docker.com/_/postgres).
The `application.properties` contains default Postgres setup configuration.

#### Application

IntelliJ setup:
- Project SDK: 11
- Project Language Level: 11
- Build, Execution, Deployment > Compiler > Annotation Processors - check "Enable annotation processing"

Build JAR with `mvn clean package`. Application and sources jars will be created in root `target` folder.

On every start of application, database tables will be created via `schema.sql` and test data will be loaded via `data.sql`.
That behaviour could be changed in `#jpa` section of `application.properties`.

#### Test APIs

http://localhost:8181/swagger-ui.html




