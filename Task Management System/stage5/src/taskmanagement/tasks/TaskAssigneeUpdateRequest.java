package taskmanagement.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TaskAssigneeUpdateRequest(
        @NotBlank
        @Pattern(regexp = "none|.+@.+\\..+")
        String assignee
) {
}
