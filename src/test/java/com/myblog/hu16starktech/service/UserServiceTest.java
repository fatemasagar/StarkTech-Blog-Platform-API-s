package com.hashedin.hu16starktech.service;

import com.hashedin.hu16starktech.exceptions.InvalidInputDataException;
import com.hashedin.hu16starktech.exceptions.UserExistsByEmailIdException;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.repositories.PostRepository;
import com.hashedin.hu16starktech.repositories.UserRepository;
import com.hashedin.hu16starktech.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    User user;
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Before
    public void setUp() throws ParseException {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("2000-09-23");

        user = new User("my_tester", "duplicate_tester@gmail.com", "tester", "Testing", "UnitTester", dob);

        try {
            User created_user = userService.createUser(this.user);
        } catch (ConstraintViolationException | InvalidInputDataException e) {
        }



    }

    @Test
    public void test_create_new_user()
    {
        Date dob = new Date(1997, 06 , 27);
        user = new User("tester", "my_tester@gmail.com", "tester", "Testing", "UnitTester", dob);

        try {
            User created_user = userService.createUser(this.user);
            Assert.assertTrue(userRepository.existsById(created_user.getUser_id()));
        } catch (ConstraintViolationException e) {
            Assert.fail("User Exits with given Email Id");
        } catch (InvalidInputDataException e)
        {
            Assert.fail("Input Data is invalid");
        }
        finally {
            userRepository.deleteById(user.getUser_id());
        }


    }

    @Test
    public void test_createInvalidDOBUser() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("2015-09-23");
        user = new User("Tester2", "my_tester2@gmail.com", "abc546", "abcOccupation", "abc@dsig", dob);
        try {
            User created_user = userService.createUser(this.user);
            Assert.fail();
        } catch (DataIntegrityViolationException e) {
            Assert.fail("Unexpected Exception");
        } catch (InvalidInputDataException e)
        {
            System.out.println("Case successful" +  e.getMessage());
        }

    }

    @Test
    public void test_createDuplicateUser() throws ParseException, Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("2000-09-23");
        user = new User("abc", "duplicate_tester@gmail.com", "abc546", "abcOccupation", "abc@dsig", dob);
        try {
            User created_user = userService.createUser(user);
            throw  new Exception("TestInvalid");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Test Case successful");
        } catch (InvalidInputDataException e)
        {
            Assert.fail("Input Data was invalid");
        }
        finally {
            userRepository.deleteById(user.getUser_id());
        }

    }


}
