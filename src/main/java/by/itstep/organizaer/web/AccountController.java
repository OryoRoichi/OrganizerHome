package by.itstep.organizaer.web;

import by.itstep.organizaer.model.dto.AccountDto;
import by.itstep.organizaer.model.dto.BillDto;
import by.itstep.organizaer.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {

    AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }

    @GetMapping("/get")
    public ResponseEntity<AccountDto> getAccountById(@RequestParam Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PatchMapping("/patch")
    public ResponseEntity<AccountDto> update(@RequestParam String name, @RequestParam Long id) {
        return ResponseEntity.ok(accountService.update(name, id));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long id) {
        accountService.delete(id);
    }

/*    @PatchMapping("/fill")
    public ResponseEntity<BillDto> fill(@RequestBody @Valid BillDto billDto){
        return
    }*/
}
