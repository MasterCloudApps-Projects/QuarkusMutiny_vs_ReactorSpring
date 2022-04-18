# Spring MVC and Spring Data JPA

This application is built using the [Spring Boot](https://spring.io/projects/spring-boot), that exposes an API Rest,
which uses [Spring MVC](https://docs.spring.io/spring-framework/docs/5.3.15/reference/html/web.html#mvc), that is
connected to MySQL database using the [Spring Data JPA](https://spring.io/projects/spring-data) dependecy.

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

The application has an OpenAPI dependency, providing a Swagger interface to make requests against to the API,
click [here](http://localhost:8080/api/swagger-ui/index.html) to test it.