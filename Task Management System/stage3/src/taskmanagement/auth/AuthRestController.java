package taskmanagement.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {
    private final AuthenticationService authenticationService;

    public AuthRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/api/auth/token")
    public JwtResponse login(Authentication authentication) {
        var token = authenticationService.createAccessToken(authentication);
        return new JwtResponse(token);
    }
}
