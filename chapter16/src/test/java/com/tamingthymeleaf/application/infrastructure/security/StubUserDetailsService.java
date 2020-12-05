package com.tamingthymeleaf.application.infrastructure.security;

import com.tamingthymeleaf.application.user.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class StubUserDetailsService implements UserDetailsService {
    public static final String USERNAME_USER = "alanna.sparrow@hey.com";
    public static final String USERNAME_ADMIN = "gavin.joyce@gmail.com";

    private final Map<String, ApplicationUserDetails> users = new HashMap<>();

    public StubUserDetailsService(PasswordEncoder passwordEncoder) {
        addUser(new ApplicationUserDetails(createUser(passwordEncoder)));
        addUser(new ApplicationUserDetails(createAdmin(passwordEncoder)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(users.get(username))
                       .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private void addUser(ApplicationUserDetails userDetails) {
        users.put(userDetails.getUsername(), userDetails);
    }

    private User createUser(PasswordEncoder passwordEncoder) {
        return User.createUser(new UserId(UUID.randomUUID()),
                               new UserName("Alanna", "Sparrow"),
                               passwordEncoder.encode("secret"),
                               Gender.FEMALE,
                               LocalDate.parse("2001-06-19"),
                               new Email(USERNAME_USER),
                               new PhoneNumber("+555 123 456"));
    }

    private User createAdmin(PasswordEncoder passwordEncoder) {
        return User.createAdministrator(new UserId(UUID.randomUUID()),
                                        new UserName("Gavin", "Joyce"),
                                        passwordEncoder.encode("secret"),
                                        Gender.MALE,
                                        LocalDate.parse("2001-06-19"),
                                        new Email(USERNAME_ADMIN),
                                        new PhoneNumber("+555 123 456"));
    }
}
