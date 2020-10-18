package com.tamingthymeleaf.application.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User createUser(CreateUserParameters parameters);

    Page<User> getUsers(Pageable pageable); //<.>
}
