package com.requestProducer.repository;

import com.requestProducer.entity.CollegeAdmission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CollegeAdmissionRepository extends JpaRepository<CollegeAdmission, Long> {
    boolean existsByHallTicketNumber(Long hallTicketNumber);

    boolean existsByEmail(String email);

    Long countByInterestedStream(String interestedStream);

    CollegeAdmission findByHallTicketNumber(Long hallTicketNumber);

    @Transactional
    @Modifying
    @Query(value = "update COLLEGE_ADMISSION_REQUEST set status = ?1 where HALL_TICKET_NUMBER = ?2", nativeQuery = true)
    int updateCollegeAdmissionByHallTicketNumber(String status, Long hallTicketNumber);
}
