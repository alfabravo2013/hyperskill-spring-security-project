package taskmanagement.account;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import taskmanagement.common.ApiException;
import taskmanagement.security.SecurityUser;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmailIgnoreCase(email);
    }

    public void createAccount(AccountRegisterRequest request) {
        try {
            var account = new Account(
                    request.email().toLowerCase(),
                    passwordEncoder.encode(request.password())
            );
            accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(409, "Email '%s' already taken".formatted(request.email()));
        }
    }

    public Account getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser.getAccount();
        }

        if (principal instanceof Jwt jwt) {
            var username = jwt.getSubject();
            return findAccountByEmail(username).orElseThrow(() -> {
                        var message = "Authenticated user '%s' not found in the user store".formatted(username);
                        return new AuthenticationCredentialsNotFoundException(message);
                    }
            );
        }

        throw new IllegalStateException("Authenticated user is not SecurityUser or Jwt");
    }
}
