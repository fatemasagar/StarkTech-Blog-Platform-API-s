package com.hashedin.hu16starktech.repositories;

import com.hashedin.hu16starktech.models.Comment;
import com.hashedin.hu16starktech.models.Notification;
import com.hashedin.hu16starktech.models.Post;
import com.hashedin.hu16starktech.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificatinoRepository extends JpaRepository<Notification, Integer> {

    @Query("FROM Notification n where n.user_id = :user_id")
    List<Notification> findAllByUserId(@Param("user_id") int user_id);


}
