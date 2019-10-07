package com.hashedin.hu16starktech.service;

import com.hashedin.hu16starktech.exceptions.*;
import com.hashedin.hu16starktech.models.Comment;
import com.hashedin.hu16starktech.models.Post;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.repositories.CommentRepository;
import com.hashedin.hu16starktech.repositories.PostRepository;
import com.hashedin.hu16starktech.repositories.UserRepository;
import com.hashedin.hu16starktech.services.CommentService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    private User user;
    private Post post;
    private Comment comment = new Comment();

    @Before
    public void setUp()  throws ParseException{



    }

    /**
     * Test Cases for creating a Post
     */
    @Test //Test for a valid Post
    public void test_createComment() throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("2000-09-23");
        user = new User("tester30", "tester20@gmail.com", "tester20", "Testing", "UnitTester", dob);
        userRepository.save(user);

        Date created_at = format.parse("2019-08-28");
        post = new Post();
        post.setDescription("Testing initiated 19 ");
        post.setUser(user);
        post.setCreated_at(created_at);
        post.setUpvote(0);
        post.setDownvote(0);
        postRepository.save(post);

        comment.setPost(post);
        comment.setUser(user);
        comment.setDescription("Blogging blog");
        try{
            Comment createdComment = commentService.createComment(comment, user.getUser_id(), post.getPost_id());
            Assert.assertNotEquals(0, createdComment.getComment_id());
        } catch (UserNotFoundException e) {
            Assert.fail("User does not exist");

        } catch (PostsNotFoundException e) {
            Assert.fail("Post does not exist");
        }
        finally {
            commentRepository.deleteById(comment.getComment_id());
            postRepository.deleteById(post.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }

    }

    @Test
    public void test_createCommentForInvalidUser() throws UserNotFoundException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date created_at = format.parse("2019-08-28");
        post = new Post();

        post.setDescription("Testing initiated 22 ");
        post.setUser(user);
        post.setCreated_at(created_at);
        post.setUpvote(0);
        post.setDownvote(0);
        postRepository.save(post);
        Comment comment = new Comment();
        comment.setDescription("Bloggers are the amazing people");
        comment.setPost(post);
        try {
            Comment created_com = commentService.createComment(comment, 234, post.getPost_id());
            Assert.assertEquals(0, created_com.getComment_id());
        } catch (PostsNotFoundException e) {
            //Assert.fail("Post does not exist");
        } catch (UserNotFoundException e) {
            //Assert.assertTrue("User Not found");
            //Assert.fail("User Not Found");
        }
        finally {
            postRepository.deleteById(post.getPost_id());
        }

    }

    @Test
    public void test_createCommentForInvalidPost() throws UserNotFoundException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("2000-06-21");
        user = new User("tester31", "tester31@gmail.com", "tester31", "Testing", "UnitTester", dob);
        comment.setUser(user);
        comment.setDescription("Blogging blog");
        Comment comment = new Comment();
        comment.setDescription("Bloggers are the amazing people");
        comment.setPost(post);
        try {
            Comment created_com = commentService.createComment(comment, user.getUser_id(), 333);
            Assert.assertEquals(0, created_com.getComment_id());
        } catch (PostsNotFoundException e) {
            //Assert.fail("Post does not exist");
        } catch (UserNotFoundException e) {
            //Assert.assertTrue("User Not found");
            //Assert.fail("User Not Found");
        }
        finally {
        }

    }

    /**
     * TestCases for checking the upvote count
     */

    @Test //Valid Comment
    public void upVoteValidComment () throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("1997-12-12");
        user = new User("tester1", "tester1@gmail.com", "tester1", "Testing", "UnitTester", dob);
        userRepository.save(user);

        Date created_at = format.parse("2019-08-28");
        post = new Post();
        post.setDescription("Testing initiated 1 ");
        post.setUser(user);
        post.setCreated_at(created_at);
        post.setUpvote(0);
        post.setDownvote(0);
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setDescription("Testing testing");
        commentRepository.save(comment);

        try {
            int previouscount = comment.getUpvote();
            Comment commentupdated = commentService.upVoteComment(comment.getComment_id(), user.getUser_id());
            int newcount = commentupdated.getUpvote();
            Assert.assertTrue(newcount-previouscount == 1);
        } catch (OwnUpvoteException e) {

        } catch (CommentNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {

        }
        finally {
            commentRepository.deleteById(comment.getComment_id());
            postRepository.deleteById(post.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }
    }


    @Test
    public void upVoteInValidComment () throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("1997-12-12");
        user = new User("tester2", "tester2@gmail.com", "tester2", "Testing", "UnitTester", dob);
        userRepository.save(user);

        Date created_at = format.parse("2019-08-28");
        post = new Post();
        post.setDescription("Testing initiated 2 ");
        post.setUser(user);
        post.setCreated_at(created_at);
        post.setUpvote(0);
        post.setDownvote(0);
        postRepository.save(post);

        Comment comment = new Comment();

        try {
            int previouscount = comment.getUpvote();
            Comment commentupdated = commentService.upVoteComment( 864, user.getUser_id());
            int newcount = commentupdated.getUpvote();
            Assert.assertTrue(newcount-previouscount == 1);
        } catch (OwnUpvoteException e) {

        } catch (CommentNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {

        }
        finally {
            postRepository.deleteById(post.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }

    }

    @Test
    public void upVoteCommentLimitExceeded () throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("1997-12-12");
        user = new User("tester3", "tester3@gmail.com", "tester3", "Testing", "UnitTester", dob);
        userRepository.save(user);

        Date created_at = format.parse("2019-08-28");
        post = new Post();
        post.setDescription("Testing initiated 3 ");
        post.setUser(user);
        post.setCreated_at(created_at);
        post.setUpvote(0);
        post.setDownvote(0);
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setDescription("Testing testing");
        comment.setUpvote(Integer.MAX_VALUE);
        commentRepository.save(comment);

        try {
            int previouscount = comment.getUpvote();
            Comment commentupdated = commentService.upVoteComment( comment.getComment_id(), user.getUser_id());
            int newcount = commentupdated.getUpvote();
            Assert.assertTrue(newcount-previouscount == 1);
        } catch (OwnUpvoteException e) {

        } catch (CommentNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {

        }
        finally {
            commentRepository.deleteById(comment.getComment_id());
            postRepository.deleteById(post.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }

    }

    @Test //Valid Comment
    public void downVoteValidComment () throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("1997-12-12");
        user = new User("tester1", "tester1@gmail.com", "tester1", "Testing", "UnitTester", dob);
        userRepository.save(user);

        Date created_at = format.parse("2019-08-28");
        post = new Post();
        post.setDescription("Testing initiated 1 ");
        post.setUser(user);
        post.setCreated_at(created_at);
        post.setUpvote(0);
        post.setDownvote(0);
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setDescription("Testing testing");
        commentRepository.save(comment);

        try {
            int previouscount = comment.getUpvote();
            Comment commentupdated = commentService.downVoteComment(comment.getComment_id(), user.getUser_id());
            int newcount = commentupdated.getUpvote();
            Assert.assertTrue(newcount-previouscount == 1);
        } catch (OwnDownvoteException e) {

        } catch (CommentNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {

        }
        finally {
            commentRepository.deleteById(comment.getComment_id());
            postRepository.deleteById(post.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }
    }


    @Test
    public void downVoteInValidComment () throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("1997-12-12");
        user = new User("tester2", "tester2@gmail.com", "tester2", "Testing", "UnitTester", dob);
        userRepository.save(user);

        Date created_at = format.parse("2019-08-28");
        post = new Post();
        post.setDescription("Testing initiated 2 ");
        post.setUser(user);
        post.setCreated_at(created_at);
        post.setUpvote(0);
        post.setDownvote(0);
        postRepository.save(post);

        Comment comment = new Comment();

        try {
            int previouscount = comment.getUpvote();
            Comment commentupdated = commentService.downVoteComment( 864, user.getUser_id());
            int newcount = commentupdated.getUpvote();
            Assert.assertTrue(newcount-previouscount == 1);
        } catch (OwnDownvoteException e) {

        } catch (CommentNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {

        }
        finally {
            postRepository.deleteById(post.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }

    }

    @Test
    public void downVoteCommentLimitExceeded () throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = format.parse("1997-12-12");
        user = new User("tester3", "tester3@gmail.com", "tester3", "Testing", "UnitTester", dob);
        userRepository.save(user);

        Date created_at = format.parse("2019-08-28");
        post = new Post();
        post.setDescription("Testing initiated 3 ");
        post.setUser(user);
        post.setCreated_at(created_at);
        post.setUpvote(0);
        post.setDownvote(0);
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setDescription("Testing testing");
        comment.setUpvote(Integer.MAX_VALUE);
        commentRepository.save(comment);

        try {
            int previouscount = comment.getUpvote();
            Comment commentupdated = commentService.downVoteComment( comment.getComment_id(), user.getUser_id());
            int newcount = commentupdated.getUpvote();
            Assert.assertTrue(newcount-previouscount == 1);
        } catch (OwnDownvoteException e) {

        } catch (CommentNotFoundException e) {

        } catch (UserNotFoundException e) {

        } catch (InternalLimitExceeded internalLimitExceeded) {

        }
        finally {
            commentRepository.deleteById(comment.getComment_id());
            postRepository.deleteById(post.getPost_id());
            userRepository.deleteById(user.getUser_id());
        }

    }






}
