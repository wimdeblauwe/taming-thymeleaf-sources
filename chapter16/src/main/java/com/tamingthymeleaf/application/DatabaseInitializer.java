package com.tamingthymeleaf.application;

import com.google.common.collect.Streams;
import com.tamingthymeleaf.application.team.CreateTeamParameters;
import com.tamingthymeleaf.application.team.PlayerPosition;
import com.tamingthymeleaf.application.team.TeamPlayerParameters;
import com.tamingthymeleaf.application.team.TeamService;
import com.tamingthymeleaf.application.user.*;
import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Component
@Profile("init-db")
public class DatabaseInitializer implements CommandLineRunner {
    private static final String[] TEAM_NAMES = new String[]{
            "Initiates",
            "Knights",
            "Padawans",
            "Rookies",
            "Wizards"
    };
    private final Faker faker = new Faker();
    private final UserService userService;
    private final TeamService teamService;

    public DatabaseInitializer(UserService userService, TeamService teamService) {
        this.userService = userService;
        this.teamService = teamService;
    }

    @Override
    public void run(String... args) {
        Set<User> generatedUsers = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            CreateUserParameters parameters = newRandomUserParameters();
            generatedUsers.add(userService.createUser(parameters));
        }

        UserName userName = randomUserName();
        CreateUserParameters parameters = new CreateUserParameters(userName,
                                                                   userName.getFirstName(),
                                                                   randomGender(),
                                                                   LocalDate.parse("2000-01-01"),
                                                                   generateEmailForUserName(userName),
                                                                   randomPhoneNumber());
        userService.createAdministrator(parameters);

        // tag::create-teams[]
        Streams.forEachPair(generatedUsers.stream().limit(TEAM_NAMES.length),
                            Arrays.stream(TEAM_NAMES),
                            (user, teamName) -> {
                                Set<TeamPlayerParameters> players = Set.of(
                                        new TeamPlayerParameters(randomUser(generatedUsers),
                                                                 PlayerPosition.SMALL_FORWARD),
                                        new TeamPlayerParameters(randomUser(generatedUsers),
                                                                 PlayerPosition.SHOOTING_GUARD),
                                        new TeamPlayerParameters(randomUser(generatedUsers),
                                                                 PlayerPosition.CENTER)
                                );
                                CreateTeamParameters teamParameters = new CreateTeamParameters(teamName,
                                                                                               user.getId(),
                                                                                               players);
                                teamService.createTeam(teamParameters);
                            });
        // end::create-teams[]
    }

    private UserId randomUser(Set<User> users) {
        int index = faker.random().nextInt(users.size());
        Iterator<User> iter = users.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next().getId();
    }

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
