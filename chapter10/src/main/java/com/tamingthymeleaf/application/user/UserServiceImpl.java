package com.tamingthymeleaf.application.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(CreateUserParameters parameters) {
        UserId userId = repository.nextId();
        User user = new User(userId,
                             parameters.getUserName(),
                             parameters.getGender(),
                             parameters.getBirthday(),
                             parameters.getEmail(),
                             parameters.getPhoneNumber());
        return repository.save(user);
    }

    // tag::getUsers[]
    @Override
    public Page<User> getUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }
    // end::getUsers[]
}
