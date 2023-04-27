package com.epam.app;

import com.epam.app.client.SportsClient;
import com.epam.app.model.dto.Sport;
import com.epam.app.service.SportService;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.core.publisher.Flux;

//@EnableWebFlux
@SpringBootApplication
public class Application {
    @Autowired
    private SportsClient sportsClient;

    @Autowired
    private SportService sportService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Flux<Sport> take = sportsClient.retrieveSports()
                    .take(10);

            take.subscribe(System.out::println);

            sportService
                    .saveAll(take)
                    .subscribe(System.out::println);
        };
    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabaseCleaner(new ResourceDatabasePopulator(new ClassPathResource("cleanup.sql")));

        return initializer;
    }

}
