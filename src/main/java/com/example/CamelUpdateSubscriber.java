package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class CamelUpdateSubscriber implements MessageListener {
    private final CamelContext camelContext;
    private final Map<String, RouteController.RouteInfo> routeInfoRegistry;
    private final ObjectMapper mapper;

    public CamelUpdateSubscriber(CamelContext camelContext, Map<String, RouteController.RouteInfo> routeInfoRegistry) {
        this.camelContext = camelContext;
        this.routeInfoRegistry = routeInfoRegistry;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            int seconds = new Random().nextInt(10);
            System.out.println(String.format("Updating routes in listener.  Sleeping to %d seconds", seconds));
            Thread.sleep(1000 * seconds);
            RouteController.RouteInfo routeInfo = mapper.readValue(message.getBody(), RouteController.RouteInfo.class);
            camelContext.addRoutes(new SimpleRouteBuilder(routeInfo.getRouteId(), routeInfo.getUrl(), routeInfo.getOutMap()));
            routeInfoRegistry.put(routeInfo.getRouteId(), routeInfo);
            System.out.println(String.format("Route %s was added.", routeInfo.getRouteId()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
