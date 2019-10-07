package com.hashedin.hu16starktech.controllers;

import com.hashedin.hu16starktech.models.Notification;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.services.CommentService;
import com.hashedin.hu16starktech.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @RequestMapping(value = "/users/{user_id}/notifications", method = RequestMethod.GET)
    public ResponseEntity viewNotifications(@PathVariable int user_id) {
        List<Notification> notifications = notificationService.getNotifications(user_id);
        return new ResponseEntity(notifications, HttpStatus.OK);
    }


}


