package com.example.saga.worker;
import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages="com.example.saga.worker")
public class TemporalWorkerApplication { public static void main(String[] args){SpringApplication.run(TemporalWorkerApplication.class,args);} }
