package com.example.saga.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.example.saga")
@EntityScan("com.example.saga")
@EnableJpaRepositories("com.example.saga")
@EnableScheduling
public class OrderServiceApplication {
    public static void main(String[] args) { SpringApplication.run(OrderServiceApplication.class, args); }
}
