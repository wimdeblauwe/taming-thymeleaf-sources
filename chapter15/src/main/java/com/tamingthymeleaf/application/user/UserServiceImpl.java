package com.tamingthymeleaf.application.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // tag::create[]
    @Override
    public User createUser(CreateUserParameters parameters) {
        LOGGER.debug("Creating user {} ({})", parameters.getUserName().getFullName(), parameters.getEmail().asString());
        UserId userId = repository.nextId();
        String encodedPassword = passwordEncoder.encode(parameters.getPassword()); //<.>
        User user = User.createUser(userId,
                                    parameters.getUserName(),
                                    encodedPassword,
                                    parameters.getGender(),
                                    parameters.getBirthday(),
                                    parameters.getEmail(),
                                    parameters.getPhoneNumber());
        return repository.save(user);
    }

    @Override
    public User createAdministrator(CreateUserParameters parameters) {
        LOGGER.debug("Creating administrator {} ({})", parameters.getUserName().getFullName(), parameters.getEmail().asString());
        UserId userId = repository.nextId();
        User user = User.createAdministrator(userId,
                                             parameters.getUserName(),
                                             passwordEncoder.encode(parameters.getPassword()),
                                             parameters.getGender(),
                                             parameters.getBirthday(),
                                             parameters.getEmail(),
                                             parameters.getPhoneNumber());
        return repository.save(user);
    }
    // end::create[]

    // tag::editUser[]
    @Override
    public User editUser(UserId userId, EditUserParameters parameters) {
        User user = repository.findById(userId)
                              .orElseThrow(() -> new UserNotFoundException(userId)); //<.>

        if (parameters.getVersion() != user.getVersion()) { //<.>
            throw new ObjectOptimisticLockingFailureException(User.class, user.getId().asString());
        }

        parameters.update(user); //<.>
        return user;
    }
    // end::editUser[]

    @Override
    @Transactional(readOnly = true)
    public Page<User> getUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userWithEmailExists(Email email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<User> getUser(UserId userId) {
        return repository.findById(userId);
    }

    @Override
    public void deleteUser(UserId userId) {
        repository.deleteById(userId);
    }

    @Override
    public long countUsers() {
        return repository.count();
    }

    @Override
    public void deleteAllUsers() {
        repository.deleteAll();
    }
}
