package com.requestProducer.utils;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationUtils {

    @Value("${camel.sourceLocation}")
    private String sourceLocation;
    @Value("${camel.destinationLocation1}")
    private String destinationLocation1;
//    @Value("${camel.destinationLocation2}")
//    private String destinationLocation2;
//    @Value("${camel.destinationLocation3}")
//    private String destinationLocation3;
}
