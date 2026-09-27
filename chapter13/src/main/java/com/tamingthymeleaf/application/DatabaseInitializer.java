package com.tamingthymeleaf.application;

import com.tamingthymeleaf.application.user.*;
import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("init-db") //<.>
public class DatabaseInitializer implements CommandLineRunner { //<.>
    private final Faker faker = new Faker(); //<.>
    private final UserService userService;

    public DatabaseInitializer(UserService userService) { //<.>
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 20; i++) { //<.>
            CreateUserParameters parameters = newRandomUserParameters();
            userService.createUser(parameters);
        }
    }

    private CreateUserParameters newRandomUserParameters() {
        Name name = faker.name();
        UserName userName = new UserName(name.firstName(), name.lastName());
        Gender gender = faker.bool().bool() ? Gender.MALE : Gender.FEMALE;
        LocalDate birthday = faker.timeAndDate().birthday(10, 40);
        Email email = new Email(faker.internet().emailAddress(generateEmailLocalPart(userName)));
        PhoneNumber phoneNumber = new PhoneNumber(faker.phoneNumber().phoneNumber());
        return new CreateUserParameters(userName, gender, birthday, email, phoneNumber);
    }

    private String generateEmailLocalPart(UserName userName) {
        return String.format("%s.%s",
                             userName.getFirstName().toLowerCase().replace("'", ""),
                             userName.getLastName().toLowerCase().replace("'", ""));
    }
}
