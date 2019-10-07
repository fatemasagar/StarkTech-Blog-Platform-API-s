package com.hashedin.hu16starktech.services;

import com.hashedin.hu16starktech.models.Notification;
import com.hashedin.hu16starktech.models.User;
import com.hashedin.hu16starktech.repositories.NotificatinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificatinoRepository notifRepo ;

    public List<Notification> getNotifications(int user_id) {

        List<Notification> result = new ArrayList<>() ;
        List<Notification> list = notifRepo.findAllByUserId( user_id) ;

        result.addAll(list);
        return  result ;

    }

    public boolean addNotifcation ( int user_id , Notification.Type type , int post_id , int comment_id  )
    {
        Notification notification = new Notification( );
        notification.setUser_id( user_id );
        if ( post_id > 0 )
        {
            notification.setPost_id( post_id);
        }
        if (comment_id > 0)
        {
            notification.setComment_id(comment_id);
        }

        notification.setGenerated_on( type );

        notifRepo.save(notification);

        return  notification.getNotification_id() != 0 ;

    }




}
