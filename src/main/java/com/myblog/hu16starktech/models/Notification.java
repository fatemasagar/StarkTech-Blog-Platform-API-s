package com.hashedin.hu16starktech.models;

import javax.persistence.*;

@Entity
@Table(name = "Notification")
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int notification_id;

    int post_id;

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    int comment_id;

    int user_id;

    public enum Type { POST, COMMENT, UPVOTE, DOWNVOTE  };
    Type generated_on;

    public Notification() {
    }

    public Notification(int post_id, int user_id, Type generated_on) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.generated_on = generated_on;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Type getGenerated_on() {
        return generated_on;
    }

    public void setGenerated_on(Type generated_on) {
        this.generated_on = generated_on;
    }
}
