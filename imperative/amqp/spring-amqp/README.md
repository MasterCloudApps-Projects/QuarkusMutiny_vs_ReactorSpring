# Spring and RabbitMQ

This project is built using the [Spring Boot](https://spring.io/).

The application uses [Spring AMQP](https://spring.io/projects/spring-amqp) dependecy, which simplifies the configuration, we only have to configure the queue, exchange and binding.

## Set Up ⚙

You must have installed on your machine:
* JDK 11 version
* Apache Maven 3.8.1
* Docker

If you already have it installed, you must run the following command:

```bash
docker run --name rabbitmq-tfm -p 5672:5672 -p 15672:15672 -d rabbitmq:3.9.8-management
```

## Start Up 🛠

You can run the application as follows:

```bash
./mvnw spring-boot:run
```

## Testing 🔍

The application is so simply, only send a few messages to the queue, then receives them and prints them on the console.