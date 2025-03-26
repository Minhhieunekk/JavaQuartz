package com.example.javaquartz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "JOB_DATA")
@Data
@Getter
@Setter
public class JobData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_data_seq")
    @SequenceGenerator(name = "job_data_seq", sequenceName = "JOB_DATA_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "SERVICE_NAME", length = 100)
    private String serviceName;

    @Column(name = "JOB_STATUS", length = 50)
    private String jobStatus;

    @Column(name = "LAST_EXECUTION")
    private LocalDateTime lastExecution;

    @Column(name = "DATA_PAYLOAD", length = 5000)
    private String dataPayload;

    public JobData() {
    }

    public JobData(String serviceName, String jobStatus, LocalDateTime lastExecution, String dataPayload) {
        this.serviceName = serviceName;
        this.jobStatus = jobStatus;
        this.lastExecution = lastExecution;
        this.dataPayload = dataPayload;
    }
}