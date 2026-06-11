package com.paddock.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PaddockApplication {

    static void main(String[] args) {
        SpringApplication.run(PaddockApplication.class, args);
    }

}
