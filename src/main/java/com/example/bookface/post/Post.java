package com.example.bookface.post;

import com.example.bookface.appuser.AppUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    @ManyToOne
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

    public Post(String content, AppUser appUser, LocalDateTime timestamp) {
        this.content = content;
        this.timestamp = timestamp;
        this.appUser = appUser;
    }
}
