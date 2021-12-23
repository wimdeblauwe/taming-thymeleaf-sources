package com.tamingthymeleaf.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// tag::classdef[]
@SpringBootTest
@ActiveProfiles("spring-boot-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TamingThymeleafApplicationTests {
    // end::classdef[]
    @Test
    void contextLoads() {
    }
}
