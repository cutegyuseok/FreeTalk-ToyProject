package com.cutegyuseok.freetalk.posting.repository;

import com.cutegyuseok.freetalk.auth.entity.User;
import com.cutegyuseok.freetalk.posting.entity.Comment;
import com.cutegyuseok.freetalk.posting.entity.Posting;
import com.cutegyuseok.freetalk.posting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote,Long> {

    Optional<Vote> findByUserAndCommentAndPosting(User user, Comment comment, Posting posting);

}
