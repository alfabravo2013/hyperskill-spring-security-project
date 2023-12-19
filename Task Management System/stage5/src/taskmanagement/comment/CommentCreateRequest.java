package taskmanagement.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(@NotBlank String text) {
}
