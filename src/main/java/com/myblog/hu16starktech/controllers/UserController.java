package com.hashedin.hu16starktech.controllers;

import com.hashedin.hu16starktech.exceptions.InvalidInputDataException;
import com.hashedin.hu16starktech.exceptions.UserExistsByEmailIdException;
import com.hashedin.hu16starktech.exceptions.UserNotFoundException;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.services.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/login/{email}", method = RequestMethod.POST)
    public ResponseEntity login(@PathVariable String email) {
        try {
            User user = userService.getUserInfo(email);
            return new ResponseEntity(user, HttpStatus.FOUND);
        } catch (UserNotFoundException ue) {
            logger.info(""+ue);
            return new ResponseEntity("Email not found !! Kindly SignUp to continue", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    public ResponseEntity fetchUserInformation(@PathVariable int user_id) throws UserNotFoundException{
        User user = null;
        try {
            user = userService.getUser(user_id);
            logger.info("Fetching details of user in UserController");
            return new ResponseEntity(user, HttpStatus.OK);
        } catch (InvalidInputDataException e) {
            logger.error(""+e);
            return new ResponseEntity("Invalid user id", HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping( value = "/users", method = RequestMethod.GET)
    public ResponseEntity fetchUsers() {
        return new ResponseEntity(userService.getAllUsers(), HttpStatus.OK);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<User> createNewUser(@RequestBody User user) throws ConstraintViolationException, InvalidInputDataException {
        try
        {
            return new ResponseEntity<>(userService.createUser(user) , HttpStatus.OK);
        }
        catch ( ConstraintViolationException e)
        {
            logger.error("" , e);
        }
        catch ( InvalidInputDataException e)
        {
            logger.error("" , e);
        }
        return null;
    }
}
