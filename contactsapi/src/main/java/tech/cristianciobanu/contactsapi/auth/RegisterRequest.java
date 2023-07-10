package tech.cristianciobanu.contactsapi.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.cristianciobanu.contactsapi.role.Role;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "The password should be at least 8 characters long and should contain at least an uppercase, a lowercase letter, and a special character")
    private String password;
    private Set<Role> roles;
}
