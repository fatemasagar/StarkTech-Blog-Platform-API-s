package com.hashedin.hu16starktech.controllers;

import com.hashedin.hu16starktech.models.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PostControllerTest {

    @Autowired
    PostController postController;

    private Post post;


    @Before
    public void setUp() {



    }

}
