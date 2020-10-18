package com.tamingthymeleaf.application.user;

import io.github.wimdeblauwe.jpearl.InMemoryUniqueIdGenerator;
import io.github.wimdeblauwe.jpearl.UniqueIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("data-jpa-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    private final UserRepository repository;
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UserRepositoryTest(UserRepository repository,
                       JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void validatePreconditions() {
        assertThat(repository.count()).isZero();
    }

    // tag::testSaveUser[]
    @Test
    void testSaveUser() {
        UserId id = repository.nextId();
        repository.save(new User(id,
                                 new UserName("Tommy", "Walton"),
                                 Gender.MALE,
                                 LocalDate.of(2001, Month.FEBRUARY, 17),
                                 new Email("tommy.walton@gmail.com"),
                                 new PhoneNumber("202 555 0192")));

        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM tt_user", UUID.class)).isEqualTo(id.getId());
        assertThat(jdbcTemplate.queryForObject("SELECT first_name FROM tt_user", String.class)).isEqualTo("Tommy");
        assertThat(jdbcTemplate.queryForObject("SELECT last_name FROM tt_user", String.class)).isEqualTo("Walton");
        assertThat(jdbcTemplate.queryForObject("SELECT gender FROM tt_user", Gender.class)).isEqualTo(Gender.MALE);
        assertThat(jdbcTemplate.queryForObject("SELECT birthday FROM tt_user", LocalDate.class)).isEqualTo("2001-02-17");
        assertThat(jdbcTemplate.queryForObject("SELECT email FROM tt_user", String.class)).isEqualTo("tommy.walton@gmail.com");
        assertThat(jdbcTemplate.queryForObject("SELECT phone_number FROM tt_user", String.class)).isEqualTo("202 555 0192");
    }
    // end::testSaveUser[]

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UniqueIdGenerator<UUID> uniqueIdGenerator() { //<.>
            return new InMemoryUniqueIdGenerator();
        }
    }
}
