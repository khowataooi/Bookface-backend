package com.example.bookface.post;

import com.example.bookface.appuser.AppUser;
import com.example.bookface.appuser.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/posts")
public class PostController {
    private PostService postService;
    private AppUserRepository appUserRepository;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPost() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    public Post createPost(@RequestBody PostRequest postRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        AppUser appUser = appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return postService.createPost(postRequest, appUser);
    }

    @PutMapping(path = "/{id}")
    public Post editPost(@PathVariable Long id, @RequestBody Post updatedPost) {
        return postService.editPost(id, updatedPost);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable long id) {
        postService.deletePost(id);
    }
}
