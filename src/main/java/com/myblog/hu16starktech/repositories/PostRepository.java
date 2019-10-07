package com.hashedin.hu16starktech.repositories;

import com.hashedin.hu16starktech.models.Post;
import com.hashedin.hu16starktech.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("FROM Post p where p.user = :user_id")
    List<Post> findAllByUserId(@Param("user_id") User user_id);

}
