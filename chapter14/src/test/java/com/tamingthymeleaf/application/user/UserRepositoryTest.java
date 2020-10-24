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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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


    @Test
    void testSaveUser() {
        UserId id = repository.nextId();
        repository.save(User.createUser(id,
                                        new UserName("Tommy", "Walton"),
                                        "encoded-secret-pwd",
                                        Gender.MALE,
                                        LocalDate.of(2001, Month.FEBRUARY, 17),
                                        new Email("tommy.walton@gmail.com"),
                                        new PhoneNumber("202 555 0192")));

        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM tt_user", UUID.class)).isEqualTo(id.getId());
        assertThat(jdbcTemplate.queryForObject("SELECT first_name FROM tt_user", String.class)).isEqualTo("Tommy");
        assertThat(jdbcTemplate.queryForObject("SELECT last_name FROM tt_user", String.class)).isEqualTo("Walton");
        assertThat(jdbcTemplate.queryForObject("SELECT password FROM tt_user", String.class)).isEqualTo("encoded-secret-pwd");
        assertThat(jdbcTemplate.queryForObject("SELECT gender FROM tt_user", Gender.class)).isEqualTo(Gender.MALE);
        assertThat(jdbcTemplate.queryForObject("SELECT birthday FROM tt_user", LocalDate.class)).isEqualTo("2001-02-17");
        assertThat(jdbcTemplate.queryForObject("SELECT email FROM tt_user", String.class)).isEqualTo("tommy.walton@gmail.com");
        assertThat(jdbcTemplate.queryForObject("SELECT phone_number FROM tt_user", String.class)).isEqualTo("202 555 0192");
    }

    @Test
    void testFindAllPageable() {
        saveUsers(8); //<.>

        Sort sort = Sort.by(Sort.Direction.ASC, "userName.lastName", "userName.firstName"); //<.>
        assertThat(repository.findAll(PageRequest.of(0, 5, sort))) //<.>
                                                                   .hasSize(5) //<.>
                                                                   .extracting(user -> user.getUserName().getFullName()) //<.>
                                                                   .containsExactly("Tommy1 Holt", "Tommy3 Holt", "Tommy5 Holt", "Tommy7 Holt", "Tommy0 Walton"); //<.>

        assertThat(repository.findAll(PageRequest.of(1, 5, sort))) //<.>
                                                                   .hasSize(3)
                                                                   .extracting(user -> user.getUserName().getFullName())
                                                                   .containsExactly("Tommy2 Walton", "Tommy4 Walton", "Tommy6 Walton");

        assertThat(repository.findAll(PageRequest.of(2, 5, sort))).isEmpty(); //<.>
    }

    // tag::testFindAllPageable[]
    @Test
    void testExistsByEmail() {
        UserId id = repository.nextId();
        repository.save(User.createUser(id,
                                        new UserName("Tommy", "Walton"),
                                        "encoded-secret-pwd",
                                        Gender.MALE,
                                        LocalDate.of(2001, Month.FEBRUARY, 17),
                                        new Email("tommy.walton@gmail.com"),
                                        new PhoneNumber("202 555 0192")));

        entityManager.flush();

        assertThat(repository.existsByEmail(new Email("tommy.walton@gmail.com"))).isTrue();
        assertThat(repository.existsByEmail(new Email("nobody@gmail.com"))).isFalse();
    }

    @Test
    void testDelete() {
        UserId id = repository.nextId();
        repository.save(User.createUser(id,
                                        new UserName("Tommy", "Walton"),
                                        "encoded-secret-pwd",
                                        Gender.MALE,
                                        LocalDate.of(2001, Month.FEBRUARY, 17),
                                        new Email("tommy.walton@gmail.com"),
                                        new PhoneNumber("202 555 0192")));
        entityManager.flush();
        assertThat(repository.count()).isOne();

        repository.deleteById(id);

        entityManager.flush();
        entityManager.clear();

        assertThat(repository.count()).isZero();
    }

    // end::testFindAllPageable[]

    private void saveUsers(int numberOfUsers) {
        for (int i = 0; i < numberOfUsers; i++) {
            repository.save(User.createUser(repository.nextId(),
                                            new UserName(String.format("Tommy%d", i), i % 2 == 0 ? "Walton" : "Holt"),
                                            "encoded-secret-pwd", Gender.MALE,
                                            LocalDate.of(2001, Month.FEBRUARY, 17),
                                            new Email("tommy.walton" + i +
                                                              "@gmail.com"),
                                            new PhoneNumber("202 555 0192")));
        }
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UniqueIdGenerator<UUID> uniqueIdGenerator() { //<.>
            return new InMemoryUniqueIdGenerator();
        }
    }
}
