package taskmanagement.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskDto(
        String id,
        String title,
        String description,
        String status,
        String author,
        String assignee,
        @JsonProperty("total_comments") int totalComments
) {
}
