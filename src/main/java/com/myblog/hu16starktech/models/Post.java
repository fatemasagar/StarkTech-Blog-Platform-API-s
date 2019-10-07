package com.hashedin.hu16starktech.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "Posts")
public class Post {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "postGen")
    int post_id;

    String title;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn( name = "user_id")
    private User user;

    @NotBlank @Column
    String description;

    @Column()
    Date created_at, updated_at;

    int upvote = 0, downvote = 0;

    public Post() {
    }

    public Post(int post_id, User user, String title, String description, Date created_at, Date updated_at) {
        this.post_id = post_id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.upvote = 0;
        this.downvote = 0;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

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
