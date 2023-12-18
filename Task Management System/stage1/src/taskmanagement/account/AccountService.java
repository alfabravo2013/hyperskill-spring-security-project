package taskmanagement.account;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanagement.common.ApiException;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    public final PasswordEncoder passwordEncoder;

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
}
