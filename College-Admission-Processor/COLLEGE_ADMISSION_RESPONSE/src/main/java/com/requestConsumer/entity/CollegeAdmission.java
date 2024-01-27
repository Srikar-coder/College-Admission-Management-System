package com.requestConsumer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "COLLEGE_ADMISSION_REQUEST", schema = "College_Admin")
public class CollegeAdmission {
    //add spring boot annotations like ID, name length, marks limit

    @Id
    @GeneratedValue//(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
//    @SequenceGenerator(name = "sequence_generator", sequenceName = "sequence_name", allocationSize = 1)
    @Column(name = "REQUESTID")
    private Long requestId;
    @Column(name = "FIRSTNAME")
    private String firstName;
    @Column(name = "LASTNAME")
    private String lastName;
    @Column(name = "DATEOFBIRTH")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "HALLTICKETNUMBER")
    private Long hallTicketNumber;
    @Column(name = "INTERMEDIATEMARKS")
    private Integer intermediateMarks;
    @Column(name = "INTERESTEDSTREAM")
    private String interestedStream;
    @Column(name = "STATUS")
    private String status = "YET TO PROCESS";
}
