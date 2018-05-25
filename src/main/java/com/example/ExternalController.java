package com.example;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ExternalController {
    private final CamelContext camelContext;
    private final ProducerTemplate producerTemplate;

    @Autowired
    public ExternalController(final CamelContext camelContext, final ProducerTemplate producerTemplate) {
        this.camelContext = camelContext;
        this.producerTemplate = producerTemplate;
    }

    @RequestMapping(value = "/{routeId}", method = RequestMethod.POST)
    public void invoke(@RequestBody final String body, @PathVariable("routeId") final String routeId, final HttpServletResponse response) throws IOException {
        if (camelContext.hasEndpoint("direct:" + routeId) != null) {
            System.out.println("Sending: " + body);
            String resp = producerTemplate.requestBody("direct:" + routeId, body, String.class);
            response.setStatus(200);
            response.getWriter().write(resp);
        } else {
            response.setStatus(404);
        }
    }
}
