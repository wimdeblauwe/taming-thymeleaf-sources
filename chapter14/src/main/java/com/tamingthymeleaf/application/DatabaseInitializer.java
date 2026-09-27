package com.tamingthymeleaf.application;

import com.tamingthymeleaf.application.user.*;
import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

//tag::class-until-run[]
@Component
@Profile("init-db")
public class DatabaseInitializer implements CommandLineRunner {
    private final Faker faker = new Faker();
    private final UserService userService;

    public DatabaseInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 20; i++) { //<.>
            CreateUserParameters parameters = newRandomUserParameters();
            userService.createUser(parameters);
        }

        UserName userName = randomUserName();
        CreateUserParameters parameters = new CreateUserParameters(userName,
                                                                   userName.getFirstName(), // <.>
                                                                   randomGender(),
                                                                   LocalDate.parse("2000-01-01"),
                                                                   generateEmailForUserName(userName),
                                                                   randomPhoneNumber());
        userService.createAdministrator(parameters); //<.>
    }

    //end::class-until-run[]

    private CreateUserParameters newRandomUserParameters() {
        UserName userName = randomUserName();
        Gender gender = randomGender();
        LocalDate birthday = faker.timeAndDate().birthday(10, 40);
        Email email = generateEmailForUserName(userName);
        PhoneNumber phoneNumber = randomPhoneNumber();
        return new CreateUserParameters(userName, userName.getFirstName(), gender, birthday, email, phoneNumber);
    }

    private UserName randomUserName() {
        Name name = faker.name();
        return new UserName(name.firstName(), name.lastName());
    }

    private Gender randomGender() {
        return faker.bool().bool() ? Gender.MALE : Gender.FEMALE;
    }

    private Email generateEmailForUserName(UserName userName) {
        return new Email(faker.internet().emailAddress(generateEmailLocalPart(userName)));
    }

    private PhoneNumber randomPhoneNumber() {
        return new PhoneNumber(faker.phoneNumber().phoneNumber());
    }

    private String generateEmailLocalPart(UserName userName) {
        return String.format("%s.%s",
                             userName.getFirstName().toLowerCase().replace("'", ""),
                             userName.getLastName().toLowerCase().replace("'", ""));
    }
}
