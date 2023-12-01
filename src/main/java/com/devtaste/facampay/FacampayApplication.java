package com.devtaste.facampay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FacampayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacampayApplication.class, args);
    }

}
