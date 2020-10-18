package com.tamingthymeleaf.application.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserRepository extends PagingAndSortingRepository<User, UserId>, UserRepositoryCustom {
}
