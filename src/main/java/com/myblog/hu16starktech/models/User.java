package com.hashedin.hu16starktech.models;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(nullable = false)
    int user_id;

    @NotBlank @Column(unique = true)
    String name;

    @NotBlank @Column(unique = true)
    String email;

    @NotBlank
    String password;
    @NotBlank
    String occupation;
    @NotBlank
    String designation;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = Post.class)
    private List<Post> posts;

    Date date_of_birth;

    File avatar;

    User() {}

    public User(String name, String email, String password, String occupation, String designation, Date date_of_birth) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.occupation = occupation;
        this.designation = designation;
        this.date_of_birth = date_of_birth;
        //this.avatar = avatar;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
    }
}
