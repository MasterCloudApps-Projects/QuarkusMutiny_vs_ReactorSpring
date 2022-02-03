# Quarkus and WebSockets

This project is based to [Quarkus](https://quarkus.io/).

The application provides a chat room where different users can connect in a room and chat with each other.

## Set Up ⚙

You must have installed on your machine:
* JDK 11 version
* Apache Maven 3.8.1

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

The project provides user interface to [try it](http://localhost:8080). You can open some tabs in your browser, connect
with different names and send some messages to see how it works