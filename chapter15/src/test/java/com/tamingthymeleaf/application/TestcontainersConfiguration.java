package com.tamingthymeleaf.application;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false) //<.>
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection //<.>
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:18"); //<.>
    }
}
