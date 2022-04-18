# Spring WebFlux and Data Access with R2DBC

This project is built using the [Spring Boot](https://spring.io/projects/spring-boot).

The application exposes a Reactive API Rest, which uses [Spring WebFlux](https://docs.spring.io/spring-framework/docs/5.3.15/reference/html/web.html#mvc), that is connected to MySQL database using the [Data Access with R2DBC](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#r2dbc) dependency for reactive connection.

Unfortunately, the Data Access with R2DBC does not support JPQL and it is necessary to write simple SQL queries.

## Set Up ⚙

You must have installed on your machine:
* JDK 11 version
* Apache Maven 3.8.1
* Docker

If you already have it installed, you need to [configure](https://github.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/tree/main/setup#configuring-the-mysql-database-) the MySQL database.

## Start Up 🛠

You can run the application as follows:

```bash
./mvnw spring-boot:run
```

## Testing 🔍

The application has an OpenAPI dependency, providing a Swagger interface to make requests against to the API, click [here](http://localhost:8080/api/webjars/swagger-ui/index.html) to test it.