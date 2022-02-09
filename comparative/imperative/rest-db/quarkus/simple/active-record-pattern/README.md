# Quarkus API Rest and Hibernate ORM with Panache's Active Record Pattern

This application is built using the [Quarkus Framework](https://quarkus.io/), that exposes an API Rest, which uses [RESTEasy](https://resteasy.dev/) implementation of the JAX-RS specification, that is connected to MySQL database using the Panache's _Active Record Pattern_.

## Set Up ⚙

You must have installed on your machine:

* JDK 11 version
* Apache Maven 3.8.1
* Docker

If you already have it installed, you must run the following command:

```bash
docker run --name mysql-tfm -d -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=imdb -p 3306:3306 mysql:8.0.26
```

Next, you should wait a few seconds (5s - 10s) for MySQL will finish booting. Now, you need
to [download](https://raw.githubusercontent.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/main/imperative/rest-db/imdb_movies.sql)
the script for the database, then you can execute the following command:

```bash
docker exec -i mysql-tfm mysql -uroot -ppassword imdb < imdb_movies.sql
```

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

The application has an OpenAPI dependency, providing a Swagger interface to make requests against to the API,
click [here](http://localhost:8080/swagger-ui/) to test it.