package com.epam.app;

import com.epam.app.config.RulesConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner runner(RulesConfig rulesConfig) {
        return args -> {
            System.out.println(rulesConfig.getRules());
        };
    }
}
