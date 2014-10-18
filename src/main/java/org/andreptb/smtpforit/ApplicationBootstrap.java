package org.andreptb.smtpforit;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAutoConfiguration
@ComponentScan("org.andreptb.smtpforit")
public class ApplicationBootstrap {


    public static void main(String[] args) {
        SpringApplication.run(ApplicationBootstrap.class, args);
    }
}
