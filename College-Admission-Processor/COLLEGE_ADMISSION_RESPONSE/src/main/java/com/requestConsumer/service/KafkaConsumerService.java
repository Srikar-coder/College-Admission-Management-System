package com.requestConsumer.service;

import com.requestConsumer.entity.CollegeAdmission;
import com.requestConsumer.repository.CollegeAdmissionRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.requestConsumer.constants.ApplicationConstants.ADMISSION_REQUEST_TOPIC;

@Service
@Log4j2
public class KafkaConsumerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CollegeAdmissionRepository collegeAdmissionRepository;

    @KafkaListener(topics = ADMISSION_REQUEST_TOPIC, groupId = "admission-group")
    public void consumeMessage(ConsumerRecord<String, CollegeAdmission> record) {
        CollegeAdmission request = record.value();
        // Process the Kafka message as needed
        if(collegeAdmissionRepository.countByInterestedStream(request.getInterestedStream()) > 5){
            log.info("Currently admissions are filled for {}, please try another stream.",
                    request.getInterestedStream());
        }
        // Save the message to the database using Spring Data JPA
        else{
            request.setStatus("Processed");
            collegeAdmissionRepository.save(request);
        }
    }
}
