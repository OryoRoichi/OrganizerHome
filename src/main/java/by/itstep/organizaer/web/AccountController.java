package by.itstep.organizaer.web;

import by.itstep.organizaer.model.dto.AccountDto;
import by.itstep.organizaer.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {

    AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }
    @GetMapping("/get")
    public ResponseEntity<AccountDto> getAccountById(@RequestParam Long id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
}
