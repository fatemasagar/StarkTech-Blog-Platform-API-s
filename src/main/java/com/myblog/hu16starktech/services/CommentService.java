package com.hashedin.hu16starktech.services;

import com.hashedin.hu16starktech.exceptions.*;
import com.hashedin.hu16starktech.models.Comment;
import com.hashedin.hu16starktech.models.Notification;
import com.hashedin.hu16starktech.models.Post;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.repositories.CommentRepository;
import com.hashedin.hu16starktech.repositories.PostRepository;
import com.hashedin.hu16starktech.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationService notificationService ;

    Logger logger = LoggerFactory.getLogger(CommentService.class);

    public List<Comment> getComents(int post_id) throws PostsNotFoundException {
        List<Comment> allComments = new ArrayList<>();

        Optional<Post> post = postRepository.findById(post_id);
        if ( post.isPresent() ) {
            for (Comment comment : this.commentRepository.findAllByPostId(post.get())) {
                allComments.add(comment);
            }
            return allComments;
        }
        else
        {
            throw new PostsNotFoundException("No post found for Post Id: "+ post_id);
        }
    }

    public Comment createComment(Comment comment, int user_id, int post_id) throws PostsNotFoundException, UserNotFoundException {
        if ( postRepository.existsById(post_id) ) {
            if(userRepository.existsById(user_id)) {
                logger.info("Post exits in db with Post Id: " + post_id);
                Post post = postRepository.findById(post_id).get();
                comment.setPost(post);
                User user = userRepository.findById(user_id).get();
                comment.setUser(user);

                if ( !notificationService.addNotifcation( user_id, Notification.Type.POST , post_id , -1 ) )
                {
                    logger.warn("Failed to create notification for this comment");
                }

                return commentRepository.save(comment);
            }
            else {
                throw new UserNotFoundException("User not found with User Id:"+user_id);
            }
        }
        else {
            throw new PostsNotFoundException("Post not found with Post Id: "+post_id);
        }
    }

    public Comment upVoteComment(int comment_id, int user_id) throws OwnUpvoteException, CommentNotFoundException, UserNotFoundException, InternalLimitExceeded {
        if (commentRepository.existsById(comment_id)) {
            if (userRepository.existsById(user_id)) {
                Comment comment = commentRepository.findById(comment_id).get();

                if ( comment.getUpvote() == Integer.MAX_VALUE )
                {
                    throw new InternalLimitExceeded("Upvote Limits for this post have exceeded");
                }

                if (comment.getUser().getUser_id() != user_id) {
                    int upVote = comment.getUpvote();
                    comment.setUpvote(upVote + 1);
                    commentRepository.save(comment);

                    if ( !notificationService.addNotifcation( user_id, Notification.Type.UPVOTE , -1 , comment_id ) )
                    {
                        logger.warn("Failed to create notification for this upvote");
                    }

                    return comment;
                }
                else {
                    throw new OwnUpvoteException("Trying to upvote own post");
                }
            } else {
                throw new UserNotFoundException("User Not found while upvoting");
            }
        }
        else {
            throw new CommentNotFoundException("Comment not found while downvoting");
        }
    }

    public Comment downVoteComment(int comment_id, int user_id) throws OwnDownvoteException, CommentNotFoundException, UserNotFoundException, InternalLimitExceeded {
        if (commentRepository.existsById(comment_id)) {
            if (userRepository.existsById(user_id)) {
                Comment comment = commentRepository.findById(comment_id).get();
                if (comment.getUser().getUser_id() != user_id) {
                    int downVote = comment.getDownvote();
                    if (downVote == Integer.MAX_VALUE)
                        throw new InternalLimitExceeded("Max limit for downvotes reached for comment ID : " + comment_id);
                    comment.setDownvote(downVote + 1);
                    commentRepository.save(comment);

                    if ( !notificationService.addNotifcation( user_id, Notification.Type.DOWNVOTE , -1 , comment_id ) )
                    {
                        logger.warn("Failed to create notification for this downvote");
                    }

                    return comment;
                } else {
                    throw new OwnDownvoteException("Trying to downvote own post");
                }
            } else {
                throw new UserNotFoundException("User Not found while downvoting");
            }
        }
        else {
            throw new CommentNotFoundException("Comment not found while downvoting");
        }
    }

    public List<Comment> getAllUserComments(int user_id) throws InvalidInputDataException{
        List<Comment> allComments = new ArrayList<>();

        if ( !validateUserId(user_id) ) {
            throw new InvalidInputDataException();
        }
        Optional<User> user = userRepository.findById(user_id);
        if ( userRepository.existsById(user_id) )
        {
            for (Comment comment : this.commentRepository.findAllByUserId(user.get())) {
                allComments.add(comment);
            }
        }
        return allComments;
    }

    private boolean validateUserId(int user_id) {
        if(user_id < 0 )
            return false;
        else
            return true;
    }
}
