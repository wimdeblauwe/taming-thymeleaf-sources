package com.tamingthymeleaf.application.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends PagingAndSortingRepository<User, UserId>, UserRepositoryCustom {
    boolean existsByEmail(Email email);

    Optional<User> findByEmail(Email email);
}
