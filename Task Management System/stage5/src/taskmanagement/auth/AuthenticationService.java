package taskmanagement.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import taskmanagement.account.AccountService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthenticationService {
    private static final long TOKEN_TTL = 3600;
    private final AccountService accountService;
    private final JwtEncoder jwtEncoder;

    public AuthenticationService(AccountService accountService,
                                 JwtEncoder jwtEncoder) {
        this.accountService = accountService;
        this.jwtEncoder = jwtEncoder;
    }

    public String createAccessToken(Authentication authentication) {
        var account = accountService.getCurrentUser();
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .claim("scope", authorities)
                .subject(account.getEmail())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(TOKEN_TTL, ChronoUnit.SECONDS))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
