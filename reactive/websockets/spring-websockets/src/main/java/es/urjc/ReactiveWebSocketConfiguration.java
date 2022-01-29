package es.urjc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.Map;

@Configuration
public class ReactiveWebSocketConfiguration {

    @Bean
    public HandlerMapping webSocketHandlerMapping(WebSocketHandler webSocketHandler) {
        Map<String, WebSocketHandler> map = Map.of("/room", webSocketHandler);
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(10);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

}