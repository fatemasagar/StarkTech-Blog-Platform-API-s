package com.hashedin.hu16starktech.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int comment_id;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    String description;

    Date created_at, updated_at;

    int upvote, downvote;

//    @OneToMany
//    @JoinColumn ( name = "user_id" )
//    User voting_user_id ;

    public Comment() {
    }

    public Comment(Post post, String description, Date created_at, Date updated_at, int upvote, int downvote) {
        this.post = post;
        this.description = description;
        this.created_at = new Date();
        this.updated_at = updated_at;
        this.upvote = 0;
        this.downvote = 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }
}
