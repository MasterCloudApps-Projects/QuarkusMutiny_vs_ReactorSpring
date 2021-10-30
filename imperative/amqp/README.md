# AMQP Project

This project uses RabbitMQ as AMQP messaging.

## Set Up

After running the application, you have to start a RabbitMQ docker container, you can use the following command:

```shell script
docker run --name rabbitMQ -p 5672:5672 -p 15672:15672 -d rabbitmq:3.9.8-management
```

## Running the application

You can run your application using:

```shell script
./mvnw compile quarkus:dev
```
