package com.tamingthymeleaf.application;

import com.tamingthymeleaf.application.user.UserService;
import io.github.wimdeblauwe.testcontainers.cypress.CypressContainer;
import io.github.wimdeblauwe.testcontainers.cypress.CypressTest;
import io.github.wimdeblauwe.testcontainers.cypress.CypressTestResults;
import io.github.wimdeblauwe.testcontainers.cypress.CypressTestSuite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("integration-test")
public class CypressE2eTests {
    @Container
    private static final PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName("tamingthymeleafdb")
            .withUsername("user")
            .withPassword("secret");

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeEach
    void validatePreconditions() {
        assertThat(userService.countUsers()).isZero();
    }

    @TestFactory //<.>
    List<DynamicContainer> runTests() throws InterruptedException, IOException, TimeoutException { //<.>
        // Ensure that the Cypress container can access the Spring Boot app running on port `port` via `host.testcontainers.internal`
        org.testcontainers.Testcontainers.exposeHostPorts(port);
        try (CypressContainer container = new CypressContainer("cypress/included:5.1.0")
                .withLocalServerPort(port)) {
            container.start();
            CypressTestResults testResults = container.getTestResults();

            return convertToJUnitDynamicTests(testResults); //<.>
        }
    }

    private List<DynamicContainer> convertToJUnitDynamicTests(CypressTestResults testResults) {
        List<DynamicContainer> dynamicContainers = new ArrayList<>();
        List<CypressTestSuite> suites = testResults.getSuites();
        for (CypressTestSuite suite : suites) {
            createContainerFromSuite(dynamicContainers, suite);
        }
        return dynamicContainers;
    }

    private void createContainerFromSuite(List<DynamicContainer> dynamicContainers, CypressTestSuite suite) {
        List<DynamicTest> dynamicTests = new ArrayList<>();
        for (CypressTest test : suite.getTests()) {
            dynamicTests.add(DynamicTest.dynamicTest(test.getDescription(), () -> assertTrue(test.isSuccess())));
        }
        dynamicContainers.add(DynamicContainer.dynamicContainer(suite.getTitle(), dynamicTests));
    }
}
