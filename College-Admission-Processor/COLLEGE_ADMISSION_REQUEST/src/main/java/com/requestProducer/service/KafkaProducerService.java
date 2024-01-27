package com.requestProducer.service;

import com.requestProducer.entity.CollegeAdmission;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.requestProducer.constants.ApplicationConstants.ADMISSION_REQUEST_TOPIC;

@Service
@Slf4j
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private com.requestProducer.service.CollegeService collegeService;

    @Handler
    public void sendToKafkaAndUpdateDatabase(CollegeAdmission collegeAdmission) {
        if (!collegeService.checkDuplicateHallTicketEntry(collegeAdmission.getHallTicketNumber())
        && !collegeService.checkDuplicateEmailEntry(collegeAdmission.getEmail())) {
            collegeAdmission.setStatus("PROCESSING");
            kafkaTemplate.send(ADMISSION_REQUEST_TOPIC, collegeAdmission);
            log.info("Updating payload into Database {}", collegeAdmission);
            collegeService.saveAdmission(collegeAdmission);
            log.info("Updated successfully");
        } else {
            log.info("Record already Exists");
        }
    }
}
