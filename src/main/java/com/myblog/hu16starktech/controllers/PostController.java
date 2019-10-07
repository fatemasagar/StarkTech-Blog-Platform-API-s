package com.hashedin.hu16starktech.controllers;

import com.hashedin.hu16starktech.exceptions.*;
import com.hashedin.hu16starktech.models.Post;
import com.hashedin.hu16starktech.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@CrossOrigin
@RestController
public class PostController {

    @Autowired
    PostService postService;

    Logger logger = LoggerFactory.getLogger(PostController.class);

    @RequestMapping( value = "/posts", method = RequestMethod.GET)
    public ResponseEntity fetchPosts() {
        List<Post> postList = postService.getPostDetails();
        return new ResponseEntity(postList, HttpStatus.OK);
    }

    @RequestMapping( value = "/user/{user_id}/posts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPost(@Valid @RequestBody Post post, @PathVariable int user_id) throws ParseException {
        logger.info("Creating post...!!!");
        try {
            Post createdPost = postService.createPost(post, user_id);
            return new ResponseEntity(createdPost, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping( value = "/post/{post_id}", method = RequestMethod.GET)
    public ResponseEntity fetchSinglePost(@PathVariable int post_id) {
        logger.info("Fetching the post with id:" + post_id +"from the posts table");
        try {
            Post post = postService.getSinglePost(post_id);
            return new ResponseEntity( post,  HttpStatus.OK);
        } catch (PostsNotFoundException postsNotFoundException) {
            logger.error("", postsNotFoundException);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping( value = "/user/{user_id}/posts", method = RequestMethod.GET)
    public ResponseEntity fetchUserPosts(@Valid @PathVariable int user_id) {
        logger.info("Fetching the posts of the user");

        List<Post> result = null ;
        try {
            result = postService.getPostsByUser(user_id);
            return new ResponseEntity( result , HttpStatus.OK);
        }
        catch ( UserNotFoundException e )
        {
            logger.error( "" , e);
            return new ResponseEntity( "The request is invalid and cannot be processed. If you believe you should not be seeing this error please reach out at our customer care" , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/users/{user_id}/posts/{post_id}/upvote")
    public ResponseEntity upVotePost(@PathVariable int user_id, @PathVariable int post_id) {
        try {
            logger.info("Upvote of Post called");
            Post post = postService.upvotePost(user_id, post_id);
            return new ResponseEntity(post, HttpStatus.OK);
        }
        catch (OwnUpvoteException own) {
            logger.error(""+own);
            return new ResponseEntity("User trying to upvote their own post", HttpStatus.BAD_REQUEST);
        }
        catch (UserNotFoundException ue) {
            logger.error(""+ue);
            return new ResponseEntity("The user doesnot exists", HttpStatus.BAD_REQUEST);
        }
        catch (PostsNotFoundException pe) {
            logger.error(""+pe);
            return new ResponseEntity("The post does not exists", HttpStatus.BAD_REQUEST);
        } catch (InternalLimitExceeded internalLimitExceeded) {
            logger.error(""+internalLimitExceeded);
            return new ResponseEntity("Max limit reached", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users/{user_id}/posts/{post_id}/downvote")
    public ResponseEntity downVotePost(@PathVariable int user_id, @PathVariable int post_id) {
        try {
            logger.info("Upvote of Post called");
            Post post = postService.downvotePost(user_id, post_id);
            return new ResponseEntity(post, HttpStatus.OK);
        }
        catch (OwnDownvoteException own) {
            logger.error(""+own);
            return new ResponseEntity("User trying to downvote their own post", HttpStatus.BAD_REQUEST);
        }
        catch (UserNotFoundException ue) {
            logger.error(""+ue);
            return new ResponseEntity("The user doesnot exists", HttpStatus.BAD_REQUEST);
        }
        catch (PostsNotFoundException pe) {
            logger.error(""+pe);
            return new ResponseEntity("The post does not exists", HttpStatus.BAD_REQUEST);
        } catch (InternalLimitExceeded internalLimitExceeded) {
            logger.error(""+ internalLimitExceeded);
            return new ResponseEntity("Max limit reached", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
