package com.hashedin.hu16starktech.repositories;

import com.hashedin.hu16starktech.models.Comment;
import com.hashedin.hu16starktech.models.Post;
import com.hashedin.hu16starktech.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("FROM Comment c where c.post = :post_id")
    List<Comment> findAllByPostId( @Param("post_id") Post post_id);

    @Query("FROM Comment c where c.user = :user_id")
    List<Comment> findAllByUserId(@Param("user_id") User user_id);
}
