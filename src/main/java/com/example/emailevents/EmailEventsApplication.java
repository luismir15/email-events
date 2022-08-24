package com.example.emailevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.emailevents.model")
public class EmailEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailEventsApplication.class, args);
    }

}
