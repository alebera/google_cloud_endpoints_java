package com.crystalloids.alessandro.berardinelli.db.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
   @Id
    private Long id;
    private Long postId;
    private String author;
    private String body;
    private LocalDateTime createdAt;
}

