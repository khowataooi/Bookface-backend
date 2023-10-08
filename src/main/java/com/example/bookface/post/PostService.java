package com.example.bookface.post;

import com.example.bookface.appuser.AppUser;
import com.example.bookface.appuser.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(PostRequest request, AppUser appUser) {
        Post post = new Post(request.getContent(), appUser, LocalDateTime.now());
        return postRepository.save(post);
    }

    public Post editPost(Long id, Post updatedPost) {
        Post existingPost = postRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post with id " + id + " not found"));
        existingPost.setContent(updatedPost.getContent());
        return postRepository.save(existingPost);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
