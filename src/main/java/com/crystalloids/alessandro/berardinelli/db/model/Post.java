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
public class Post {
    @Id
    private Long id;
    private String author;
    private String subject;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isFromAuthor(String author){
        return this.author.equals(author);
    }
}
