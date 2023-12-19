package taskmanagement.comment;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentRestController {
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/api/tasks/{taskId}/comments")
    public void createComment(@PathVariable String taskId,
                              @Valid @RequestBody CommentCreateRequest request) {
        commentService.createComment(taskId, request);
    }

    @GetMapping("/api/tasks/{taskId}/comments")
    public List<CommentDto> getCommentsForTask(@PathVariable String taskId) {
        return commentService.getCommentsByTaskId(taskId).stream()
                .map(this::mapCommentToCommentDto)
                .toList();
    }

    private CommentDto mapCommentToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId().toString(),
                comment.getTask().getId().toString(),
                comment.getText(),
                comment.getAuthor().getEmail()
        );
    }
}
