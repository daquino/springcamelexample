package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.Map;

public class JsonOutProcessor implements Processor {
    private final ObjectMapper mapper;
    private final Map<String, String> outMap;

    public JsonOutProcessor(final Map<String, String> outMap) {
        this.mapper = new ObjectMapper();
        this.outMap = outMap;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        String resp = message.getBody(String.class);
        System.out.println("Response = " + resp);
        Map<String, Object> values = mapper.readValue(resp, new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> newValues = new HashMap<>();
        for (Map.Entry<String, String> entry : outMap.entrySet()) {
            newValues.put(entry.getValue(), values.get(entry.getKey()));
        }
        message.setBody(mapper.writeValueAsString(newValues), String.class);
    }
}
