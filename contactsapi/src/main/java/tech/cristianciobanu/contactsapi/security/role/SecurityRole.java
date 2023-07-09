package tech.cristianciobanu.contactsapi.security.role;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import tech.cristianciobanu.contactsapi.role.Role;

@AllArgsConstructor
public class SecurityRole implements GrantedAuthority {
    private final Role role;
    @Override
    public String getAuthority() {
        return role.getName().name();
    }
}
