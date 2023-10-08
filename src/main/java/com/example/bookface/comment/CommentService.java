package com.example.bookface.comment;

import com.example.bookface.appuser.AppUser;
import com.example.bookface.post.Post;
import com.example.bookface.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment createComment(CommentRequest request, AppUser appUser, Long postId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalStateException("Post ID not found"));
        Comment comment = new Comment(request.getContent(), LocalDateTime.now(), appUser, post);
        return commentRepository.save(comment);
    }

    public Comment editComment(Long id, Comment updatedComment) {
        Comment existingComment = commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found"));
        existingComment.setContent(updatedComment.getContent());
        return commentRepository.save(existingComment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
