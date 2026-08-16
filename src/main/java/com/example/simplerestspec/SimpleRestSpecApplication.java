package com.example.simplerestspec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleRestSpecApplication {

    public static void main(String[] args) {
        com.example.simplerestspec.config.DbInitializer.initialize();
        SpringApplication.run(SimpleRestSpecApplication.class, args);
    }
}