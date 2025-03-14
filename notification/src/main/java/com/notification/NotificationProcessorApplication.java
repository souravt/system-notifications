package com.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NotificationProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationProcessorApplication.class, args);
    }
}
