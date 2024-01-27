package com.requestProducer.route;

import com.requestProducer.entity.CollegeAdmission;
import com.requestProducer.exception.NoHallTicketNumberException;
import com.requestProducer.utils.ApplicationUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.requestProducer.constants.ApplicationConstants.COMMON_ENDPOINT;
import static com.requestProducer.constants.ApplicationConstants.PRODUCER_ROUTE;
import static org.apache.camel.LoggingLevel.INFO;

@Component
public class ProducerRoute extends RouteBuilder {
    public static String fileName = "";
    @Autowired
    private ApplicationUtils applicationUtils;

    @Override
    public void configure() throws NoHallTicketNumberException {
        errorHandler(
                deadLetterChannel("direct:error")
                        .maximumRedeliveries(3)
                        .redeliveryDelay(1000)
        );

        onException(NoHallTicketNumberException.class)
                .handled(true) // Mark the exception as handled
                .log("Custom exception handled: ${exception.message}");
//                .to("direct:errorHandlingRoute");

        onCompletion()
                .log("Completed route");


//        using includeExt
//        from(applicationUtils.getSourceLocation() + "?noop=true&includeExt=json")
//                .routeId(PRODUCER_ROUTE)
//                .filter(simple("${header.fileName} endsWith '.json'"))
//                .log("${header.fileName} and ${body}");
//        using filter
        from(applicationUtils.getSourceLocation() + "?noop=true")
                .routeId(PRODUCER_ROUTE)
                .filter(simple("${header.CamelFileName} endsWith '.json'"))
                .process(exchange -> {
                    // Extract the "id" property from the JSON and set it as a header
                    String jsonString = exchange.getIn().getBody(String.class);
                    String idValue = extractIdFromJson(jsonString);
                    if(idValue == null){
                        throw new NoHallTicketNumberException("Hall ticket number is missing");
                    }
                    else
                        exchange.getIn().setHeader("hallTicketNumber", idValue);

                    // Your processing logic here
                })
                .idempotentConsumer(
                        header("hallTicketNumber"),
                        MemoryIdempotentRepository.memoryIdempotentRepository(1000)) //avoid duplicate entries
                .log(INFO,"${header.CamelFileName} and ${body}")
                .process(exchange -> {
                    fileName = exchange.getIn().getHeader("CamelFileName").toString();
                })
                .unmarshal(createAdmissionDataFormat())
                .log(INFO,"converted JSON to POJO successfully, ${body}")
                .to(COMMON_ENDPOINT);
    }

    private DataFormat createAdmissionDataFormat() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .findAndRegisterModules();
        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setObjectMapper(objectMapper);
        jacksonDataFormat.setUnmarshalType(CollegeAdmission.class);
        return jacksonDataFormat;
    }
    private String extractIdFromJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        // Assuming "id" is a direct property of the JSON object
        if(jsonNode.has("hallTicketNumber"))
           return jsonNode.get("hallTicketNumber").asText();
        else
        return null;
    }
}
