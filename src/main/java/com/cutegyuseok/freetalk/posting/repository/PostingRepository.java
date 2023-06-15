package com.cutegyuseok.freetalk.posting.repository;

import com.cutegyuseok.freetalk.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting,Long> ,PostingRepositoryCustom{


}
