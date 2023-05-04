package com.epam.app.repository;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
abstract class AbstractTestcontainersRepositoryTest {
    @Container
    static MySQLContainer MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withUsername("testUser")
            .withPassword("testPassword")
            .withDatabaseName("testDatabase");

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:mysql://%s:%s"
                .formatted(MYSQL_CONTAINER.getHost(), MYSQL_CONTAINER.getMappedPort(MySQLContainer.MYSQL_PORT)));
        registry.add("spring.r2dbc.name", MYSQL_CONTAINER::getDatabaseName);
        registry.add("spring.r2dbc.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.r2dbc.password", MYSQL_CONTAINER::getPassword);
    }
}
