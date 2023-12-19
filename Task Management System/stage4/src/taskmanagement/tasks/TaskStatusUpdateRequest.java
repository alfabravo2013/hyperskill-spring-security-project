package taskmanagement.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TaskStatusUpdateRequest(
        @NotBlank
        @Pattern(regexp = "(?i)(CREATED|IN_PROGRESS|COMPLETED)")
        String status
) {
}
