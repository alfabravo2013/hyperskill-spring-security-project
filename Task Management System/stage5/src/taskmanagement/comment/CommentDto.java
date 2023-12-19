package taskmanagement.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentDto(
        String id,
        @JsonProperty("task_id") String taskId,
        String text,
        String author
) {
}
