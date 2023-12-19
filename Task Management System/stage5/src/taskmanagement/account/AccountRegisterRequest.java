package taskmanagement.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AccountRegisterRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Length(min = 6)
        String password
) {
}
