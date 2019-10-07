package com.hashedin.hu16starktech.services;

import com.hashedin.hu16starktech.exceptions.InvalidInputDataException;
import com.hashedin.hu16starktech.exceptions.UserNotFoundException;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.repositories.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    public User getUser(int user_id) throws InvalidInputDataException{
        return userRepository.findById(user_id).get();
    }

    public User createUser(User user) throws ConstraintViolationException, InvalidInputDataException {
        Date dob = user.getDate_of_birth();
        Date currentDate = new Date();

        long diff  = currentDate.getTime() - dob.getTime() ;

        if ( currentDate.getTime() - dob.getTime() < 12L * 365L * 24L  * 3600L * 1000L )
        {
            throw new InvalidInputDataException();
        }

        return userRepository.save(user);

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserInfo(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        return user;
    }
}
