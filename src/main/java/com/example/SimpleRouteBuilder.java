package com.example;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.util.Map;

public class SimpleRouteBuilder extends RouteBuilder {
    private final String routeId;
    private final String url;
    private final Map<String, String> outMap;

    public SimpleRouteBuilder(final String routeId, final String url, final Map<String, String> outMap) {
        this.routeId = routeId;
        this.url = url;
        this.outMap = outMap;
    }

    @Override
    public void configure() {
        from(String.format("direct:%s", routeId))
                .routeId(routeId)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to(url)
                .process(new JsonOutProcessor(outMap));

    }
}
