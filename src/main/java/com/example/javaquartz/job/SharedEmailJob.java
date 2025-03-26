package com.example.javaquartz.job;

import com.example.javaquartz.entity.JobData;
import com.example.javaquartz.repository.JobDataRepository;
import com.example.javaquartz.service.EmailSenderService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@DisallowConcurrentExecution
public class SharedEmailJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(SharedEmailJob.class);
    private JobDataRepository jobDataRepository;
    private  EmailSenderService emailSenderService;

    @Value("${service.name}")
    private String serviceName;

    public SharedEmailJob() {

    }

    @Autowired
    public SharedEmailJob(JobDataRepository jobDataRepository, EmailSenderService emailSenderService) {
        this.jobDataRepository = jobDataRepository;
        this.emailSenderService = emailSenderService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobData lastExecution = jobDataRepository.findTopByOrderByLastExecutionDesc();
        if (lastExecution != null &&
                "RUNNING".equals(lastExecution.getJobStatus()) &&
                lastExecution.getLastExecution().plusMinutes(5).isAfter(LocalDateTime.now())) {
            logger.info("Job is currently being executed by another service. Skipping execution.");
            return;
        }

        JobData jobData = null;
        try {
            // Create new job execution record
            jobData = new JobData(serviceName, "RUNNING", LocalDateTime.now(),
                    "Service " + serviceName + " is executing the job");
            jobData = jobDataRepository.save(jobData);

            logger.info("Starting email job execution for service: {}", serviceName);

            // Add transaction management here if needed
            emailSenderService.sendSimpleMail(
                    "hieutmhe180168@fpt.edu.vn",
                    "Test Email from " + serviceName,
                    "Test email sent at " + LocalDateTime.now()
            );

            jobData.setJobStatus("COMPLETED");
            jobData.setDataPayload("Email sent successfully from service: " + serviceName);
            jobDataRepository.save(jobData);
            logger.info("Email job completed successfully for service: {}", serviceName);

        } catch (Exception e) {
            logger.error("Error executing email job for service: " + serviceName, e);
            if (jobData != null) {
                jobData.setJobStatus("FAILED");
                jobData.setDataPayload("Error: " + e.getMessage());
                jobDataRepository.save(jobData);
            }
            throw new JobExecutionException(e);
        }
    }
}