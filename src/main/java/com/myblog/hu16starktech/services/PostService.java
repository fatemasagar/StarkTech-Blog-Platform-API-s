package com.hashedin.hu16starktech.services;

import com.hashedin.hu16starktech.exceptions.*;
import com.hashedin.hu16starktech.models.Notification;
import com.hashedin.hu16starktech.models.Post;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.repositories.PostRepository;
import com.hashedin.hu16starktech.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationService notificationService ;

    public List<Post> getPostDetails() {
        List<Post> postList = postRepository.findAll();
        return postList;
    }

    public Post createPost(Post post, int user_id) throws UserNotFoundException {
        if (userRepository.existsById(user_id)) {
            logger.info("user_id verified in PostService");
            return postRepository.save(post);

        }
        else {
            throw new UserNotFoundException("User not found while creating post");
        }
    }

    public Post getSinglePost(int post_id) throws PostsNotFoundException {
        if(checkIfExists(post_id)) {
            return postRepository.findById(post_id).get();
        }
        else {
            throw new PostsNotFoundException("Post not found in table posts");
        }
    }

    private boolean checkIfExists(int post_id) {
        if (postRepository.existsById(post_id)) {
            return true;
        }
        else {
            return false;
        }
    }


    public List<Post> getPostsByUser(int user_id) throws UserNotFoundException {
        List<Post> allPosts =new ArrayList<>();

        Optional<User> user = userRepository.findById(user_id);
        if ( user.isPresent() )
        {
            for (Post post: this.postRepository.findAllByUserId(user.get())) {

                allPosts.add(post);
            }
            return allPosts;
        }
        else {
            throw new UserNotFoundException("No matching user found for User ID : " + user_id);
        }


    }

    public Post upvotePost(int user_id, int post_id) throws OwnUpvoteException, PostsNotFoundException, UserNotFoundException, InternalLimitExceeded {

        if (userRepository.existsById(user_id)) {
            if (postRepository.existsById(post_id)) {
                Post post = postRepository.findById(post_id).get();

                // Avoiding User to upvote on his own post.

                if (user_id != post.getUser().getUser_id()) {
                    int upvotes = post.getUpvote();
                    if (upvotes == Integer.MAX_VALUE)
                    {
                        throw new InternalLimitExceeded("Upvote Limit exceeded for this post with post ID : " + post_id) ;
                    }
                    post.setUpvote(upvotes + 1);
                    postRepository.save(post);

                    if ( !notificationService.addNotifcation( user_id, Notification.Type.UPVOTE , post_id , -1 ) )
                    {
                        logger.warn("Failed to create notification for this comment");
                    }

                    return post ;

                }
                else {
                    throw new OwnUpvoteException("Trying to upvote their own posts");
                }
            }
            else {
                throw new PostsNotFoundException("Post does not exist with id:"+post_id);
            }
        }
        else {
            throw new UserNotFoundException("user does not exist with id:"+user_id);
        }
    }

    public Post downvotePost(int user_id, int post_id) throws OwnDownvoteException, PostsNotFoundException, UserNotFoundException, InternalLimitExceeded {
        if (userRepository.existsById(user_id)) {
            if (postRepository.existsById(post_id)) {
                Post post = postRepository.findById(post_id).get();

                // Avoiding User to upvote on his own post.

                if (user_id != post.getUser().getUser_id()) {
                    int downvotes = post.getDownvote();
                    if (downvotes == Integer.MAX_VALUE)
                    {
                        throw new InternalLimitExceeded("Max limit of downvotes reached for post ID : " + post_id);
                    }
                    post.setDownvote(downvotes + 1);
                    postRepository.save(post);
                    if ( !notificationService.addNotifcation( user_id, Notification.Type.UPVOTE , post_id , -1 ) )
                    {
                        logger.warn("Failed to create notification for this comment");
                    }
                    return post;

                }
                else {
                    throw new OwnDownvoteException("Trying to downvote their own posts");
                }
            }
            else {
                throw new PostsNotFoundException("Post does not exist with id:"+post_id);
            }
        }
        else {
            throw new UserNotFoundException("user does not exist with id:"+user_id);
        }
    }
}
