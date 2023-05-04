package com.epam.app.repository;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataR2dbcTest
public class SportRepositoryTest extends AbstractTestcontainersRepositoryTest {
    @Autowired
    private SportRepository sportRepository;

    @Test
    void existsByNameTest() {
        var name = "Swimming";

        var result = sportRepository.existsByName(name);

        StepVerifier.create(result)
                .consumeNextWith(actual -> {
                    assertEquals(true, actual);
                })
                .verifyComplete();
    }

    @Test
    void findByNameContaining() {
        var query = "biking";

        var result = sportRepository.findByNameContaining(query);

        StepVerifier.create(result)
                .consumeNextWith(actual -> {
                    assertEquals(4L, actual.getId());
                    assertEquals("Hybrid biking", actual.getName());
                })
                .consumeNextWith(actual -> {
                    assertEquals(5L, actual.getId());
                    assertEquals("Mountain biking", actual.getName());
                })
                .verifyComplete();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

            ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
            initializer.setConnectionFactory(connectionFactory);

            CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
            populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
            populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
            initializer.setDatabasePopulator(populator);

            return initializer;
        }
    }
}
