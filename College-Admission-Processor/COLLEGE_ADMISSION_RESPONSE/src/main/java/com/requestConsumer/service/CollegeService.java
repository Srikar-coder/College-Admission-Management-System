package com.requestConsumer.service;

import com.requestConsumer.entity.CollegeAdmission;
import com.requestConsumer.repository.CollegeAdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {

    @Autowired
    private CollegeAdmissionRepository repository;

    public List<CollegeAdmission> getAllEntities() {
        return repository.findAll();
    }

    public void saveAdmission(CollegeAdmission admission) {
        repository.save(admission);
    }

    public boolean checkDuplicateHallTicketEntry(Long hallTicketNumber){
        return repository.existsByHallTicketNumber(hallTicketNumber);
    }

    public Long countEntriesofStream(String interestedStream){
        return repository.countByInterestedStream(interestedStream);
    }
    // Other service methods
}

