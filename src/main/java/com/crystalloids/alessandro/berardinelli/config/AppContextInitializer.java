package com.crystalloids.alessandro.berardinelli.config;

import com.crystalloids.alessandro.berardinelli.db.model.Comment;
import com.crystalloids.alessandro.berardinelli.db.model.Post;
import com.google.firebase.FirebaseApp;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextInitializer implements ServletContextListener {
       @Override
        public void contextInitialized(ServletContextEvent sce) {
           // Initialize Objectify
            ObjectifyService.init();
            ObjectifyService.register(Post.class);
            ObjectifyService.register(Comment.class);

           // Initialize Firebase Admin SDK
           FirebaseApp.initializeApp();
        }

       @Override
        public void contextDestroyed(ServletContextEvent sce) {
        }
}
