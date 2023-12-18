package taskmanagement.tasks;

import jakarta.validation.constraints.NotBlank;

public record TaskCreateRequest(
        @NotBlank
        String title,

        @NotBlank
        String description
) {
}
