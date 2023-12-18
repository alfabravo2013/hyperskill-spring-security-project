package taskmanagement.account;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRestController {
    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/api/accounts")
    public void createAccount(@Valid @RequestBody AccountRegisterRequest request) {
        accountService.createAccount(request);
    }
}
