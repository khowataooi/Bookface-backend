package com.example.bookface.comment;

import com.example.bookface.appuser.AppUser;
import com.example.bookface.post.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;
    @ManyToOne
    @JoinColumn(nullable = false, name = "post_id")
    private Post post;

    public Comment(String content,LocalDateTime timestamp, AppUser appUser, Post post) {
        this.content = content;
        this.timestamp = timestamp;
        this.appUser = appUser;
        this.post = post;
    }
}
