package com.cutegyuseok.freetalk.auth.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> ,UserRepositoryCustom{
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Boolean existsByEmail(String email);
    List<User> findAllByPkIn(List<Long> list);
}
