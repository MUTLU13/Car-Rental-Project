package com.sau.sbrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sau.sbrental")

public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}