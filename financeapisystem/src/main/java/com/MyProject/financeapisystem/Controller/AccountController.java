package com.MyProject.financeapisystem.Controller;


import com.MyProject.financeapisystem.Dtos.CreateRequestDto;
import com.MyProject.financeapisystem.Dtos.CreateResponseDto;
import com.MyProject.financeapisystem.Dtos.TransactionResponseDto;
import com.MyProject.financeapisystem.Service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Validated
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<CreateResponseDto >getAccountDetails(@PathVariable Long id){
        CreateResponseDto response = accountService.getAccount(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateResponseDto> createAccount(@Valid @RequestBody CreateRequestDto createRequestDto){
        CreateResponseDto response = accountService.createAccount(createRequestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateResponseDto> updateAccountDetails(@PathVariable Long id , @Valid @RequestBody CreateRequestDto createRequestDto){
        CreateResponseDto response = accountService.updateAccountDetails(id,createRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delelteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully");

    }
    @PostMapping("/withdraw/{id}")
    public ResponseEntity<CreateResponseDto> withdrawMoney(@PathVariable Long id, @Positive @RequestParam BigDecimal amount){
        CreateResponseDto response = accountService.withdraw(id,amount);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/deposit/{id}")
    public ResponseEntity<CreateResponseDto> depositMoney(@PathVariable Long id, @Positive @RequestParam BigDecimal money){
        CreateResponseDto response = accountService.deposit(id,money);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<CreateResponseDto> transferMoney(@RequestParam Long fromId, @RequestParam Long toId, @Positive @RequestParam BigDecimal money){
        CreateResponseDto response = accountService.transfer(fromId,toId,money);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransaction(@PathVariable Long accountId){
        return ResponseEntity.ok(accountService.getTransactionByAccount(accountId));
    }

    @GetMapping({"/{accountId}/transactions", "/transaction/{accountId}"})
    public ResponseEntity<Page<TransactionResponseDto>> getTransactions(
            @PathVariable Long accountId,
            @PositiveOrZero @RequestParam(defaultValue = "0") int page,
            @Positive @RequestParam(defaultValue = "10") int pageSize){
        return ResponseEntity.ok(accountService.getTransactionByAccountSize(accountId,page,pageSize));
    }
}
