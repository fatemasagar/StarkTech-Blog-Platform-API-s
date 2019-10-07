package com.hashedin.hu16starktech.service;

import com.hashedin.hu16starktech.controllers.PostController;
import com.hashedin.hu16starktech.exceptions.*;
import com.hashedin.hu16starktech.models.Post;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.repositories.PostRepository;
import com.hashedin.hu16starktech.repositories.UserRepository;
import com.hashedin.hu16starktech.services.PostService;
import com.hashedin.hu16starktech.services.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PostServiceTest {

    @Autowired
    PostController postController;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    private User user;
    private Post post;

    @Before
    public void setUp() {

    }

    /**
     *
     * createPost TestCases
     */

    @Test
    public void test_createPost() {

        Date dob = new Date(1988/9/23);
        user = new User( "creator", "creator@email", "creator@password", "fire@occupation", "taher@designation", dob);
        try {
            userService.createUser(user);
        } catch (ConstraintViolationException constraintError) {
            constraintError.printStackTrace();
        }
        catch (InvalidInputDataException e) {
            e.printStackTrace();
        }

        post = new Post();
        post.setUser(user);
        post.setDescription("React docs . ");
        post.setUpvote(0);
        post.setDownvote(1);
        Post createdPost = new Post();
        try {
            createdPost = postService.createPost(post, user.getUser_id());
            Assert.assertNotEquals(0, createdPost.getPost_id());

        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            postRepository.deleteById(createdPost.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }
    }


    @Test
    public void test_createPostWithNonExistingUser() throws UserNotFoundException {
        try {
            post = new Post();
            post.setDescription("Creating Exceptions");
            post.setUpvote(0);
            post.setDownvote(1);
            Post post_created = postService.createPost(post, 345);
        }
        catch (UserNotFoundException userNotFound) {

        }
    }

    @Test
    public void test_upVotePost()
    {
        Date dob = new Date(1988/9/23);
        user = new User( "upvote", "upvote@email", "upvote@password", "fire@occupation", "taher@designation", dob);
        try {
            userService.createUser(user);
        } catch (ConstraintViolationException constraintError) {
            constraintError.printStackTrace();
        }
        catch (InvalidInputDataException e)
        {
            e.printStackTrace();
        }

        post = new Post();
        post.setUser(user);
        post.setDescription("React do docs . ");
        post.setUpvote(0);
        post.setDownvote(1);
        Post createdPost = new Post();
        try {
            createdPost = postService.createPost(post, user.getUser_id());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        User voter = new User( "voter2", "voter2@email", "voter2@password", "voter@occupation", "voter@designation", dob);
        userRepository.save(voter);

        try {
            int previousUpvoteCount = post.getUpvote();
            Post upvoted = postService.upvotePost( voter.getUser_id() , createdPost.getPost_id());
            Assert.assertTrue( upvoted.getUpvote()-previousUpvoteCount == 1 );
        } catch (OwnUpvoteException e) {
            e.printStackTrace();
        } catch (PostsNotFoundException e) {
            e.printStackTrace();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        } catch (InternalLimitExceeded internalLimitExceeded) {
            internalLimitExceeded.printStackTrace();
        } finally {
            postRepository.deleteById(createdPost.getPost_id());
            userRepository.deleteById(voter.getUser_id());
            userRepository.deleteById(user.getUser_id());
        }


    }


    @Test
    public void test_upVotePost_InvalidUser()
    {
        Date dob = new Date(1988/9/23);
        user = new User( "upInvalid", "upinvalid@email", "upinvalid@password", "fire@occupation", "taher@designation", dob);
        userRepository.save(user);
        post = new Post();
        post.setUser(user);
        post.setDescription("Reacting docs . ");
        post.setUpvote(0);
        post.setDownvote(1);
        Post createdPost = null;
        try {
            createdPost = postService.createPost(post, user.getUser_id());

        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            postService.upvotePost( 576 , createdPost.getPost_id());
        } catch (OwnUpvoteException e) {
            e.printStackTrace();
        } catch (PostsNotFoundException e) {
            e.printStackTrace();
        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {
            internalLimitExceeded.printStackTrace();
        } finally {
            postRepository.deleteById(createdPost.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }
    }

    @Test
    public void test_upVotePost_MaxLimit()
    {
        Date dob = new Date(1988/9/23);
        user = new User( "fire", "fire@email", "fire@password", "fire@occupation", "taher@designation", dob);
        userRepository.save(user);
        post = new Post();
        post.setUser(user);
        post.setTitle("react");
        post.setDescription("React docs . ");
        post.setUpvote(Integer.MAX_VALUE);
        post.setDownvote(1);
        Post createdPost = new Post();
        try {
            createdPost = postService.createPost(post, user.getUser_id());

        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            postService.upvotePost( user.getUser_id() , createdPost.getPost_id());
        } catch (OwnUpvoteException e) {

        } catch (PostsNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {
            internalLimitExceeded.printStackTrace();
        }
    }

    @Test
    public void test_downVotePost()
    {
        Date dob = new Date(1988/9/23);
        user = new User( "firing", "firing@email", "firing@password", "fire@occupation", "taher@designation", dob);
        try {
            userService.createUser(user);
        } catch (ConstraintViolationException constraintError) {
            constraintError.printStackTrace();
        }
        catch (InvalidInputDataException e)
        {
            e.printStackTrace();
        }
        User voter = new User( "voter", "voter@email", "voter@password", "voter@occupation", "voter@designation", dob);
        userRepository.save(voter);
        post = new Post();
        post.setUser(user);
        post.setDescription("React -- docs . ");
        post.setUpvote(0);
        post.setDownvote(1);
        Post createdPost = new Post();
        try {
            createdPost = postService.createPost(post, user.getUser_id());


        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            int previousUpvoteCount = post.getUpvote();
            postService.downvotePost( voter.getUser_id() , createdPost.getPost_id());
            Assert.assertTrue( createdPost.getUpvote() == previousUpvoteCount );
        } catch (OwnDownvoteException e) {

        } catch (PostsNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {
            internalLimitExceeded.printStackTrace();
        }
        finally {
            postRepository.deleteById(createdPost.getPost_id());
            userRepository.deleteById(voter.getUser_id());
            userRepository.deleteById(user.getUser_id());
        }
    }

    @Test
    public void test_downVotePost_InvalidUser()
    {
        Date dob = new Date(1988/9/23);
        user = new User( "downvoteinvalid", "downvoteinvalid@email", "invalid@password", "fire@occupation", "taher@designation", dob);
        userRepository.save(user);

        post = new Post();
        post.setUser(user);
        post.setDescription("React documentation of project . ");
        post.setUpvote(0);
        post.setDownvote(1);
        Post createdPost = null;
        try {
            createdPost = postService.createPost(post, user.getUser_id());

        } catch (UserNotFoundException e) {

        }

        try {
            postService.downvotePost( 576 , createdPost.getPost_id());
        } catch (OwnDownvoteException e) {

        } catch (PostsNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {
            internalLimitExceeded.printStackTrace();
        } finally {
            postRepository.deleteById(createdPost.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }
    }

    @Test
    public void test_downVotePost_MaxLimit()
    {
        Date dob = new Date(1988/9/23);
        user = new User( "maxlimit", "maxlimit@email", "maxlimit@password", "fire@occupation", "taher@designation", dob);
        userRepository.save(user);
        post = new Post();
        post.setUser(user);
        post.setDescription("React react docs . ");
        post.setUpvote(Integer.MAX_VALUE+1);
        post.setDownvote(1);
        Post createdPost = null;
        try {
            createdPost = postService.createPost(post, user.getUser_id());

        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            postService.downvotePost( user.getUser_id() , createdPost.getPost_id());
        } catch (OwnDownvoteException e) {

        } catch (PostsNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {

        } finally {
            postRepository.deleteById(createdPost.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }
    }
}
