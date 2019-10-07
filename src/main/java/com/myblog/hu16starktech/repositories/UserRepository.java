package com.hashedin.hu16starktech.repositories;

import com.hashedin.hu16starktech.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("FROM User u where u.email = :email")
    User findByEmail(@Param("email") String email);
}
