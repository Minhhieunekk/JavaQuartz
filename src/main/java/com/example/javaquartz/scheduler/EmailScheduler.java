package com.example.javaquartz.scheduler;

import com.example.javaquartz.job.SharedEmailJob;
import org.quartz.JobDetail;
import org.quartz.impl.triggers.CronTriggerImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import javax.sql.DataSource;
import java.text.ParseException;

import static org.quartz.JobBuilder.newJob;

@Configuration
public class EmailScheduler {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean SchedulerFactoryBean(DataSource dataSource) throws ParseException {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setDataSource(dataSource);

        // Set custom job factory
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        bean.setJobFactory(jobFactory);

        bean.setSchedulerName("JobScheduler");
        bean.setStartupDelay(10);
        bean.setApplicationContextSchedulerContextKey("applicationContextKey");
        bean.setOverwriteExistingJobs(true);
        bean.setAutoStartup(true);

        JobDetail job = newJob(SharedEmailJob.class)
                .withDescription("Send Email Job")
                .withIdentity("EmailSenderJob", "SchedulerJob")
                .storeDurably(true)
                .build();

        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        cronTrigger.setName("EmailTrigger");
        cronTrigger.setGroup("SchedulerTrigger");
        cronTrigger.setCronExpression("0 * * * * ?");
        cronTrigger.setJobKey(job.getKey());

        bean.setTriggers(cronTrigger);
        bean.setJobDetails(job);

        return bean;
    }
}