package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:application-dev.properties")
public class RestApiSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiSpringBootApplication.class, args);
    }

}
