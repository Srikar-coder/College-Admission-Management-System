package com.requestConsumer.serdes;

import com.requestConsumer.entity.CollegeAdmission;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class CollegeAdmissionDeserializer implements Deserializer<CollegeAdmission> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public CollegeAdmission deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, CollegeAdmission.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing CollegeAdmission", e);
        }
    }
}
