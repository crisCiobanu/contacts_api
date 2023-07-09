package tech.cristianciobanu.contactsapi.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.cristianciobanu.contactsapi.auth.exception.UserAlreadyExistsException;
import tech.cristianciobanu.contactsapi.role.ERole;
import tech.cristianciobanu.contactsapi.role.Role;
import tech.cristianciobanu.contactsapi.role.RoleRepository;
import tech.cristianciobanu.contactsapi.security.jwt.JwtService;
import tech.cristianciobanu.contactsapi.security.user.SecurityUser;
import tech.cristianciobanu.contactsapi.user.User;
import tech.cristianciobanu.contactsapi.user.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(new SecurityUser(user));
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var roles = setUserRoles(request);
        if(this.userRepository.existsByUsername(request.getUsername())
                || this.userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("User with this username or email already exists!");
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(new SecurityUser(user));
        return new AuthenticationResponse(jwtToken);
    }

    private Set<Role> setUserRoles(RegisterRequest request) {
        HashSet<Role> roles = new HashSet<>();
        request.getRoles().forEach(role -> {
            Role foundRole = this.roleRepository.findByName(role.getName())
                    .orElse(this.roleRepository.save(role));
            roles.add(foundRole);
        });
        return roles;
    }
}
