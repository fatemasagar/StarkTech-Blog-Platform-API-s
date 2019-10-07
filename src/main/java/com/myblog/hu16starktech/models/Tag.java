package com.hashedin.hu16starktech.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Tag {
    @Id
    int tag_id;

    String tag_name;

    int post_id;

    public Tag() {
    }

    public Tag(int tag_id, String tag_name, int post_id) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.post_id = post_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
