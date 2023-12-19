package taskmanagement.tasks;

public record TaskDto(
        String id,
        String title,
        String description,
        String status,
        String author,
        String assignee
) {
}
