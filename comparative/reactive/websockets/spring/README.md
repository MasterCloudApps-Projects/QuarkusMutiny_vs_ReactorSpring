# Spring and WebSockets

This project is based to [Spring Boot](https://spring.io/). Here, we have made use of [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html) as well, because the reactive websocket is contained in Spring WebFlux. 

Now, we doesn't have [STOMP](https://stomp.github.io/stomp-specification-1.2.html#Abstract) or [SockJS](https://github.com/sockjs/sockjs-client) as in the imperative implementation because Spring doesn't provide this possibility. In this example, it uses the raw [WebSocket API](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-websocket-server).

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

The project provides user interface to [try it](http://localhost:8080). You can open some tabs in your browser, connect with different names and you will see how many users are connected in that moment.