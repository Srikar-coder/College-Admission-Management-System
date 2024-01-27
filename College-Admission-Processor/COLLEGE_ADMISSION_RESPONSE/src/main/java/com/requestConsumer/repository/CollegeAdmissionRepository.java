package com.requestConsumer.repository;

import com.requestConsumer.entity.CollegeAdmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeAdmissionRepository extends JpaRepository<CollegeAdmission, Long> {
    boolean existsByHallTicketNumber(Long hallTicketNumber);
    Long countByInterestedStream(String interestedStream);
}
