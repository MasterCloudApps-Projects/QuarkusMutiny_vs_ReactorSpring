# Spring MVC and Spring Data JPA

This project is built using the [Spring Boot](https://spring.io/projects/spring-boot).

The application exposes an API Rest, which uses [Spring MVC](https://docs.spring.io/spring-framework/docs/5.3.15/reference/html/web.html#mvc), that is connected to MySQL database using the [Spring Data JPA](https://spring.io/projects/spring-data) dependecy.

## Set Up ⚙

You must have installed on your machine:
* JDK 11 version
* Apache Maven 3.8.1
* Docker

If you already have it installed, you must run the following command:

```bash
docker run --name mysql-tfm -d -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=imdb -p 3306:3306 mysql:8.0.26
```

Next, you should wait a few seconds (5s - 10s) for MySQL will finish booting. Now, you need to [download](https://raw.githubusercontent.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/main/imperative/rest-db/imdb_movies.sql) the script for the database, then you can execute the following command:

```bash
docker exec -i mysql-tfm mysql -uroot -ppassword imdb < imdb_movies.sql
```

## Start Up 🛠

You can run the application as follows:

```bash
./mvnw spring-boot:run
```

## Testing 🔍

The application has an OpenAPI dependency, providing a Swagger interface to make requests against to the API, click [here](http://localhost:8080/api/swagger-ui/index.html) to test it.