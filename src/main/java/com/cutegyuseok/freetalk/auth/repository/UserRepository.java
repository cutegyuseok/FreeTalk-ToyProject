package com.cutegyuseok.freetalk.auth.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> ,UserRepositoryCustom{
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Boolean existsByEmail(String email);
}
