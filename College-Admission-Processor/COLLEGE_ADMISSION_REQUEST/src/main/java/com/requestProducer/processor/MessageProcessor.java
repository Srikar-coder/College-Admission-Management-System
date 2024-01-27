package com.requestProducer.processor;

import com.requestProducer.exception.NoHallTicketNumberException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;

import java.io.IOException;

public class MessageProcessor {
    @Handler
    public void process(Exchange exchange) throws IOException {
        String jsonString = exchange.getIn().getBody(String.class);
        String idValue = checkAllFieldsAndExtractIdFromJson(jsonString);
        exchange.getIn().setHeader("hallTicketNumber", idValue);


    }
    private static String checkAllFieldsAndExtractIdFromJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        //check message has all fields
        for(String field : getFieldNames()){
            if(!jsonNode.has(field))
                return field + " is missing";
        }
        if(jsonNode.has("hallTicketNumber"))
            return jsonNode.get("hallTicketNumber").asText();
        else
            return null;
    }
    public static String[] getFieldNames(){
        return new String[]{"firstName","lastName", "dateOfBirth", "email", "intermediateMarks", "hallTicketNumber",
                "interestedStream"};
    }
}
