# Spring and WebSockets

This project is based to [Spring Framework](https://spring.io/).

The application provides a chat room where different users can connect in a room and chat with each other. But we can find some differences with Quarkus. 

Here, we are using [STOMP](https://stomp.github.io/stomp-specification-1.2.html#Abstract) and [SockJS](https://github.com/sockjs/sockjs-client) as well, the reason for this is because Spring "recommends" to use this configuration instead of using the raw WebSocket API. This particular configuration does more dificult to undestand but at the same time gives more powerful.

A quick resume:
* **STOMP (Simple Text Oriented Messaging Protocol)**: is a text-oriented protocol that provides headers and body, and works as messaging brokers. This is useful because the clients can subscribe to any topic.
* **SockJS**: tries to use native WebSockets, and only if it can't it tries to use another substitute protocol.

## Set Up ⚙

You must have installed on your machine:
* JDK 11 version
* Apache Maven 3.8.1

## Start Up 🛠

You can run the application as follows:

```bash
./mvnw spring-boot:run
```

## Testing 🔍

The project provides user interface to [try it](http://localhost:8080). You can open some tabs in your browser, connect
with different names and send some messages to see how it works