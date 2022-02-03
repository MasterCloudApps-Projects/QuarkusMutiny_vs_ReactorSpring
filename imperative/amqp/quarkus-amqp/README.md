# Quarkus and RabbitMQ

This project is built using the [Quarkus Framework](https://quarkus.io/).

The application uses RabbitMQ as AMQP messaging, in this case, the Maven dependency used belongs to "Quarkus Universe", that is, someone from the Quarkus community has created it, and it hasn't been created by the Quarkus core development team.

But why don't we use the "official" dependency? because the official one is based on reactive implementation.

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

    It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Now, the only thing left to do is to run the application:

    ```bash
    java -jar target/quarkus-app/quarkus-run.jar
    ```

## Testing 🔍

The application is so simply, only send a few messages to the queue, then receives them and prints them on the console.