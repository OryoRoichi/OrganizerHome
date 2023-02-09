package by.itstep.organizaer.web;


import by.itstep.organizaer.model.dto.AccountDto;
import by.itstep.organizaer.model.dto.ArchiveStatsDto;
import by.itstep.organizaer.model.dto.analytics.AbstractAnalyticsResponseDto;
import by.itstep.organizaer.model.dto.analytics.AnalyticsTxRequestDto;
import by.itstep.organizaer.model.dto.enums.ArchiveStatsType;
import by.itstep.organizaer.service.AccountService;
import by.itstep.organizaer.service.AnalyticsService;
import by.itstep.organizaer.service.ArchivationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {

    AccountService accountService;
    ArchivationService archivationService;

    AnalyticsService analyticsService;

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

    @GetMapping("/get-archive-stats")
    public ResponseEntity<? extends ArchiveStatsDto> getArchiveStats(@RequestParam Long id, @RequestParam ArchiveStatsType type) {
        return ResponseEntity.ok(archivationService.getStats(id, type));
    }

    @GetMapping("/get-analitycs")
    public ResponseEntity<? extends AbstractAnalyticsResponseDto> getAnalitycs(@RequestParam Long accountId,
                                                                               @RequestParam ArchiveStatsType type,
                                                                               @RequestParam(required = false) LocalDateTime dateFrom,
                                                                               @RequestParam(required = false) LocalDateTime dateTo,
                                                                               @RequestParam(required = false) Float gtThan,
                                                                               @RequestParam(required = false) Float lsThan) {
        return ResponseEntity.ok(analyticsService.getTxAnalytics(AnalyticsTxRequestDto.builder()
                .type(type)
                .accountId(accountId)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .greaterThan(gtThan)
                .lessThan(lsThan)
                .build()));
    }
}
