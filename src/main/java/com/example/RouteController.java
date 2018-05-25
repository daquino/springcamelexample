package com.example;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("route")
public class RouteController {
    private final CamelContext camelContext;
    private final ProducerTemplate producerTemplate;
    private final String VEHICLE_URL = "http://www.mocky.io/v2/5b0790c12f0000b118c620ff";
    private final String BUSINESS_URL = "http://www.mocky.io/v2/5b08589a3200005a00700276";
    private final Map<String, RouteInfo> routeInfoRegistry = new ConcurrentHashMap<>();

    @Autowired
    public RouteController(final CamelContext camelContext, final ProducerTemplate producerTemplate) {
        this.camelContext = camelContext;
        this.producerTemplate = producerTemplate;
    }

    @RequestMapping(value = "/{routeId}", method = RequestMethod.GET)
    public @ResponseBody RouteInfo get(@PathVariable("routeId") final String routeId) {
        return routeInfoRegistry.get(routeId);
    }

    @RequestMapping(value = "/invoke/{routeId}", method = RequestMethod.POST)
    public void invoke(@RequestBody String body, @PathVariable("routeId") String routeId, HttpServletResponse response) throws IOException {
        if (camelContext.hasEndpoint("direct:" + routeId) != null && routeInfoRegistry.containsKey(routeId)) {
            System.out.println("Sending: " + body);
            String resp = producerTemplate.requestBody("direct:" + routeId, body, String.class);
            response.setStatus(200);
            response.getWriter().write(resp);
        } else {
            response.setStatus(404);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody RouteInfo routeInfo, HttpServletResponse response) throws Exception {
        camelContext.addRoutes(
                new SimpleRouteBuilder(routeInfo.getRouteId(), routeInfo.getUrl(), routeInfo.getOutMap())
        );
        routeInfoRegistry.put(routeInfo.routeId, routeInfo);
        response.setStatus(200);
    }

    @RequestMapping(value = "/{routeId}", method = RequestMethod.DELETE)
    public void stop(@PathVariable("routeId") String routeId, HttpServletResponse response) throws Exception {
        camelContext.stopRoute(routeId);
        camelContext.removeRoute(routeId);
        routeInfoRegistry.remove(routeId);
        response.setStatus(200);
    }

    public static class RouteInfo {
        private String routeId;
        private String url;
        private Map<String, String> outMap;

        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, String> getOutMap() {
            return outMap;
        }

        public void setOutMap(Map<String, String> outMap) {
            this.outMap = outMap;
        }
    }
}
