# Quarkus and Reactive RabbitMQ

This project is built using the [Quarkus Framework](https://quarkus.io/) and [SmallRye Reactive Messaging](https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/3.4/amqp/amqp.html).

Thanks to the SmallRye Reactive Messaging library, the configuration is so easy, we only have to add in the _application.properties_ the incoming and outgoing values, that is to say, from where the events will be published and where it'll be consumed.

We doesn't have to create any queue, exchange or binding, the library does it for us

## Set Up ⚙

You must have installed on your machine:
* JDK 11 version
* Apache Maven 3.8.1
* Docker

## Start Up 🛠

There are two option:

1. Development

    You can run your application in dev mode, Quarkus will start up a RabbitMQ docker container for you:

    ```bash
    ./mvnw clean compile quarkus:dev
    ```

2. Production

    For this case, you will responsable to start up a RabbitMQ docker container:

    ```bash
    docker run --name rabbitmq-tfm -p 5672:5672 -p 15672:15672 -d rabbitmq:3.9.8-management
    ```
    
    Now, the application must be packaged using:

    ```bash
    ./mvnw package
    ```

    It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Now, the only thing left to do is to run the application:

    ```bash
    java -jar target/quarkus-app/quarkus-run.jar
    ```

## Testing 🔍

_What does the application do?_ The application consists of writing a city, and depending on the letter that the city starts with, it will return the forecast in upper or lower case.

There is an [user interface](http://localhost:8080/forecast.html), where the user write any city and it'll send the request to the server, this send a message to the RabbitMQ queue, and the city will wait to receive the forecast.

In the meantime, the RabbitMQ listener will receive those messages, according to the name of the city will get a forecast to send it for another queue. This queue is listening for a input stream where the data is immediately sending as "Server-sent events" by http, updating the city forecast.