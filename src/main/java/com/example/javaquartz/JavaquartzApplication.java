package com.example.javaquartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableJpaRepositories("com.example.javaquartz.repository")
@EntityScan("com.example.javaquartz.entity")
@EnableTransactionManagement
public class JavaquartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaquartzApplication.class, args);
    }

}
