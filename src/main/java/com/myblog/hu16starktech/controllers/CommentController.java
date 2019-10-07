package com.hashedin.hu16starktech.controllers;

import com.hashedin.hu16starktech.exceptions.*;
import com.hashedin.hu16starktech.models.Comment;
import com.hashedin.hu16starktech.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    Logger logger = LoggerFactory.getLogger(CommentController.class);

    @RequestMapping(value = "/posts/{post_id}/comments", method = RequestMethod.GET)
    public ResponseEntity getCommentsForPost(@PathVariable int post_id) {
        try {
            List<Comment> comments = commentService.getComents(post_id);
            return new ResponseEntity(comments, HttpStatus.OK);
        } catch (PostsNotFoundException e) {
            logger.error("" + e);
            return new ResponseEntity("The Post was not found in the Database", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/users/{user_id}/posts/{post_id}/comments", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCommentForPost( @Valid @RequestBody Comment comment, @PathVariable int user_id ,@PathVariable int post_id) {
        try{
            Comment commented = commentService.createComment(comment, user_id, post_id);
            return new ResponseEntity(commented, HttpStatus.OK);
        } catch (PostsNotFoundException pe) {
            logger.error(""+pe);
            return new ResponseEntity("The post was not found while commenting", HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException ue) {
            logger.error(" "+ue);
            return new ResponseEntity("The user was not found while commenting", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/users/{user_id}/comments/{comment_id}/upvote", method = RequestMethod.PUT)
    public ResponseEntity upVoteComment(@PathVariable int comment_id, @PathVariable int user_id) {
        try {
            Comment comment = commentService.upVoteComment(comment_id, user_id);
            return new ResponseEntity(comment, HttpStatus.OK);
        }
        catch (OwnUpvoteException own) {
            logger.error(" "+own);
            return new ResponseEntity("Trying to upvote own comment", HttpStatus.BAD_REQUEST);
        }
        catch (CommentNotFoundException ce) {
            logger.error(" "+ce);
            return new ResponseEntity("Comment no found ", HttpStatus.BAD_REQUEST);
        }
        catch (UserNotFoundException ue) {
            logger.error(" "+ue);
            return new ResponseEntity("User not found ", HttpStatus.BAD_REQUEST);
        } catch (InternalLimitExceeded internalLimitExceeded) {
            logger.error(" "+ internalLimitExceeded);
            return new ResponseEntity("Could not process due to internal error ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users/{user_id}/comments/{comment_id}/downvote", method = RequestMethod.PUT)
    public ResponseEntity downVoteComment(@PathVariable int comment_id, @PathVariable int user_id) {
        try {
            Comment comment = commentService.downVoteComment(comment_id, user_id);
            return new ResponseEntity(comment, HttpStatus.OK);
        }
        catch (OwnDownvoteException own) {
            logger.error(" "+own);
            return new ResponseEntity("Trying to downvote own comment", HttpStatus.OK);
        }
        catch (CommentNotFoundException ce) {
            logger.error(" "+ce);
            return new ResponseEntity("Comment no found ", HttpStatus.BAD_REQUEST);
        }
        catch (UserNotFoundException ue) {
            logger.error(" "+ue);
            return new ResponseEntity("User not found ", HttpStatus.BAD_REQUEST);
        } catch (InternalLimitExceeded internalLimitExceeded) {
            logger.error(" "+ internalLimitExceeded);
            return new ResponseEntity("Max Upvotes on this comment have been reached ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users/{user_id}/comments", method = RequestMethod.GET)
    public ResponseEntity getUserComments(@PathVariable int user_id) {
        try {
            List<Comment> commentList = commentService.getAllUserComments(user_id);
            return new ResponseEntity(commentList, HttpStatus.OK);
        }catch (InvalidInputDataException iie) {
            logger.error(" "+iie);
            return new ResponseEntity("Comment no found ", HttpStatus.BAD_REQUEST);
        }
    }
}
