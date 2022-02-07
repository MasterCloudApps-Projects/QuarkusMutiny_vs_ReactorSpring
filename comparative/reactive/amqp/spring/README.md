# Spring Project Reactor and Reactor RabbitMQ

This project is based to [Spring Project Reactor](https://projectreactor.io/) and [Reactor RabbitMQ](https://projectreactor.io/docs/rabbitmq/snapshot/reference/).

In this case, the application uses [Reactor RabbitMQ]() instead of [Spring AMQP](https://spring.io/projects/spring-amqp) dependecy. Now, it requires a bit more configuration but it's still straightforward. As in the imperative implementation, we need to configure the queue, exchange and binding, but in addition we configure a reactive connection.

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

_What does the application do?_ The application consists of writing a city, and depending on the letter that the city starts with, it will return the forecast in upper or lower case.

There is an [user interface](http://localhost:8080/forecast.html), where the user write any city and it'll send the request to the server, this send a message to the RabbitMQ queue, and the city will wait to receive the forecast.

In the meantime, the RabbitMQ listener will receive those messages, according to the name of the city will get a forecast to send it for another queue. This queue is listening for a input stream where the data is immediately sending as "Server-sent events" by http, updating the city forecast.