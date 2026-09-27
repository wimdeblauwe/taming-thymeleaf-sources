package com.tamingthymeleaf.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

// tag::classdef[]
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class TamingThymeleafApplicationTests {
    // end::classdef[]
    @Test
    void contextLoads() {
    }
}
