package com.example.echo;

import com.example.echo.model.Comment;
import com.example.echo.model.Post;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ObjectifyInitializer implements ServletContextListener {
       @Override
        public void contextInitialized(ServletContextEvent sce) {
            ObjectifyService.init();
            ObjectifyService.register(Post.class);
            ObjectifyService.register(Comment.class);
        }

       @Override
        public void contextDestroyed(ServletContextEvent sce) {
        }
}
