package by.itstep.organizaer.web;

import by.itstep.organizaer.model.dto.CreateTxRequestDto;
import by.itstep.organizaer.model.dto.TxDto;
import by.itstep.organizaer.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/tx")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionController {

    TransactionService txService;

    @GetMapping("/get")
    public ResponseEntity<TxDto> get(@RequestParam @Min(value = 1) Long id) {
        return ResponseEntity.ok(txService.getTx(id));
    }

    @PostMapping("/create")
    public ResponseEntity<TxDto> create(@RequestBody @Valid CreateTxRequestDto request) {
        return ResponseEntity.ok(txService.doTransact(request));
    }

}
