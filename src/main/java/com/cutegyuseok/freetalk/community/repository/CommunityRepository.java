package com.cutegyuseok.freetalk.community.repository;

import com.cutegyuseok.freetalk.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    boolean existsByName(String name);
}
