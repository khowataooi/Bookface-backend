package com.example.bookface.comment;

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
@RequestMapping("/api/comments")
public class CommentController {
    private CommentService commentService;
    private AppUserRepository appUserRepository;

    @GetMapping("/{post_id}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable("post_id") Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
//        if (comments.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/{post_id}")
    public Comment createComment(@PathVariable("post_id") Long postId, @RequestBody CommentRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        AppUser appUser = appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return commentService.createComment(request, appUser, postId);
    }

    @PutMapping("/{comment_id}")
    public Comment editComment(@PathVariable("comment_id") Long commentId, @RequestParam Comment updatedComment) {
        return commentService.editComment(commentId, updatedComment);
    }

    @DeleteMapping("/{comment_id}")
    public void deleteComment(@PathVariable("comment_id") Long commentId) {
        commentService.deleteComment(commentId);
    }
}
