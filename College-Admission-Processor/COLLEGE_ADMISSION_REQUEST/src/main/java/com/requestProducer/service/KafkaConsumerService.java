package com.requestProducer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.requestProducer.entity.CollegeAdmission;
import com.requestProducer.repository.CollegeAdmissionRepository;
import com.requestProducer.utils.ApplicationUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.camel.ProducerTemplate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.requestProducer.constants.ApplicationConstants.ADMISSION_REQUEST_TOPIC;
import static com.requestProducer.route.ProducerRoute.fileName;

@Service
@Log4j2
public class KafkaConsumerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CollegeService collegeService;
    @Autowired
    private ProducerTemplate producerTemplate;
    @Autowired
    private ApplicationUtils applicationUtils;

    @KafkaListener(topics = ADMISSION_REQUEST_TOPIC, groupId = "admission-group")
    public void consumeMessage(CollegeAdmission collegeAdmission) {
//        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).findAndRegisterModules();
//        CollegeAdmission request =
        // Process the Kafka message as needed
        Long numberOfInterestedStreams = collegeService.getNumberOfInterestedStreams(collegeAdmission.getInterestedStream());
        log.info("Available entries for {} stream entries {}", collegeAdmission.getInterestedStream(), numberOfInterestedStreams);
        if (numberOfInterestedStreams > 5) {
            log.info("Currently admissions are filled for {}, please try another stream.",
                    collegeAdmission.getInterestedStream());
        }
        // Save the message to the database using Spring Data JPA
        else {

            int count = collegeService.updateDatabaseStatus(collegeAdmission);
            CollegeAdmission updatedAdmission = collegeService.findEntryByHallTicketNumber(collegeAdmission.getHallTicketNumber());
            log.info("processed with effected rows {} and record {}", count, updatedAdmission);
            producerTemplate.sendBody(applicationUtils.getDestinationLocation1() + "?fileName=" + fileName + "-processed.csv",
                    convertPojoToCsv(updatedAdmission));
            log.info("CSV file generated successfully");
        }
    }

    private String convertPojoToCsv(CollegeAdmission collegeAdmission) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Name : ").append(collegeAdmission.getFirstName() + " " + collegeAdmission.getLastName()+ System.lineSeparator())
                .append("Selected Stream : ").append(collegeAdmission.getInterestedStream() + System.lineSeparator())
                .append("Status").append(collegeAdmission.getStatus());

        return csvContent.toString();
    }
}
