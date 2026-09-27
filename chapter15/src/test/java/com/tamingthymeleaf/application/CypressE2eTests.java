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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("integration-test")
class CypressE2eTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @BeforeEach
    void validatePreconditions() {
        assertThat(userService.countUsers()).isZero();
    }

    @TestFactory //<.>
    List<DynamicContainer> runTests() throws InterruptedException, IOException, TimeoutException { //<.>
        org.testcontainers.Testcontainers.exposeHostPorts(port);
        try (CypressContainer container = new CypressContainer()
                .withLocalServerPort(port)
                .withBrowser("chrome")) {
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
