package com.example.javaquartz.repository;

import com.example.javaquartz.entity.JobData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDataRepository extends JpaRepository<JobData, Long> {
    JobData findTopByOrderByLastExecutionDesc();
}
