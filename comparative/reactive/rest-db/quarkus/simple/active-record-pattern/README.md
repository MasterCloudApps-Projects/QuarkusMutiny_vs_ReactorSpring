# Quarkus Mutiny API Rest and Hibernate ORM with Panache Reactive

This project is built using the [Quarkus Framework](https://quarkus.io/) and [Mutiny](https://smallrye.io/smallrye-mutiny/).

The application exposes a Reactive API Rest, which uses [RESTEasy](https://resteasy.dev/) implementation of the JAX-RS specification and Mutiny library for automatice transform Reactive API, that is connected to MySQL database using *Panache* (using the reactive implementation), a library similar to Spring Data JPA, simplifying the most common database operations.

Mention Panache has two difference implementations, the repository pattern and the active record pattern, in this example we use the active record pattern, this means that the entity contents all database operations.

Martin Fowler describes it in an [article](https://www.martinfowler.com/eaaCatalog/activeRecord.html) as:

> An object that wraps a row in a database table or view, encapsulates the database access, and adds domain logic on that data.

## Set Up ⚙

You must have installed on your machine:
* JDK 11 version
* Apache Maven 3.8.1
* Docker

If you already have it installed, you need to [configure](https://github.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/tree/main/setup#configuring-the-mysql-database-) the MySQL database.

## Start Up 🛠

There are two option:

1. Development

   You can run your application in dev mode:

    ```bash
    ./mvnw clean compile quarkus:dev
    ```

2. Production

   First, the application must be packaged using:

    ```bash
    ./mvnw package
    ```

   It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Now, the only thing left to do is to
   run the application:

    ```bash
    java -jar target/quarkus-app/quarkus-run.jar
    ```

## Testing 🔍

The application has an OpenAPI dependency, providing a Swagger interface to make requests against to the API, click [here](http://localhost:8080/swagger-ui/) to test it.