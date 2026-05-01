package com.MyProject.financeapisystem.Service.Impl;


import com.MyProject.financeapisystem.Dtos.CreateRequestDto;
import com.MyProject.financeapisystem.Dtos.CreateResponseDto;
import com.MyProject.financeapisystem.Dtos.TransactionResponseDto;
import com.MyProject.financeapisystem.Exception.AccountNotFoundException;
import com.MyProject.financeapisystem.Exception.InsufficientBalanceException;
import com.MyProject.financeapisystem.Exception.InvalidTransactionException;
import com.MyProject.financeapisystem.Modals.Account;
import com.MyProject.financeapisystem.Modals.Transaction;
import com.MyProject.financeapisystem.Respository.AccountRepository;
import com.MyProject.financeapisystem.Respository.TransactionRepository;
import com.MyProject.financeapisystem.Service.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    @Override
    public CreateResponseDto getAccount(Long id) {
        //Fetching data from database -> if the data is present then proceed otherwise throw exception
        Account account = findAccount(id);

        // Convert entity -> Dto
        CreateResponseDto responseDto = new CreateResponseDto();
      //  responseDto.setId(account.getId());
        responseDto.setName(account.getName());
        responseDto.setBalance(account.getBalance());

        return responseDto;
    }

    @Override
    public CreateResponseDto createAccount(CreateRequestDto createRequestDto) {
        if (createRequestDto.getBalance().compareTo(BigDecimal.ZERO) < 0){
            throw new InvalidTransactionException("Balance cannot be negative");
        }

        Account account = new Account();
        account.setName(createRequestDto.getName());
        account.setBalance(createRequestDto.getBalance());

        // Save the details in Database
        Account saveAccount = accountRepository.save(account);

        // Convert Entity -> Dto
        CreateResponseDto createResponseDto = new CreateResponseDto();
        //createResponseDto.setId(saveAccount.getId());
        createResponseDto.setName(saveAccount.getName());
        createResponseDto.setBalance(saveAccount.getBalance());
        return createResponseDto;
    }

    @Override
    public CreateResponseDto updateAccountDetails(Long id, CreateRequestDto createRequestDto) {

        Account account = findAccount(id);


        // Update only the name as it is only allowed field
        account.setName(createRequestDto.getName());

        Account updatedDetails = accountRepository.save(account);

        // convert entity -> Dto
        CreateResponseDto response = new CreateResponseDto();
        response.setName(updatedDetails.getName());
        response.setBalance(updatedDetails.getBalance());

        return response;
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = findAccount(id);
        accountRepository.delete(account);
    }

    @Override
    public CreateResponseDto withdraw(Long id, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidTransactionException("Amount should be greater than 0");
        }

        Account account = findAccount(id);

        if(account.getBalance().compareTo(amount) < 0){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        //save the updated balance in DB
        Account updated = accountRepository.save(account);

        //Saving the transaction
        Transaction transaction = new Transaction();
        transaction.setFromAccount(id);
        transaction.setAmount(amount);
        transaction.setType("WITHDRAW");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);


        // convert entity -> dto

        CreateResponseDto response = new CreateResponseDto();
        response.setName(updated.getName());
        response.setBalance(updated.getBalance());

        return response;
    }

    @Override
    public CreateResponseDto deposit(Long id, BigDecimal money) {
        if(money.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidTransactionException("Amount should be more than 0");
        }

        Account account = findAccount(id);

        account.setBalance(account.getBalance().add(money));

        // save the updated details
        Account updated = accountRepository.save(account);

        //Saving the transaction
        Transaction transaction = new Transaction();
        transaction.setToAccount(id);
        transaction.setAmount(money);
        transaction.setType("DEPOSIT");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        // convert entity -> dto

        CreateResponseDto response = new CreateResponseDto();

        response.setName(updated.getName());
        response.setBalance(updated.getBalance());

        return response;
    }

    @Override
    @Transactional
    public CreateResponseDto transfer(Long fromId, Long toId, BigDecimal money) {
        if(money.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidTransactionException("Amount should be greater than 0");
        }

        if(fromId.equals(toId)){
            throw new InvalidTransactionException("Sender and receiver cannot be same account");
        }

        Account sender = accountRepository.findById(fromId)
                .orElseThrow(()-> new AccountNotFoundException("Sender account not found"));
        Account receiver = accountRepository.findById(toId)
                .orElseThrow(()-> new AccountNotFoundException("Receiver account not found"));

        if(sender.getBalance().compareTo(money) < 0){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(money));
        receiver.setBalance(receiver.getBalance().add(money));

        // Save in Db
        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Saving the transaction
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromId);
        transaction.setToAccount(toId);
        transaction.setAmount(money);
        transaction.setType("TRANSFER");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        // convert Entity -> Dto
        CreateResponseDto responseDto = new CreateResponseDto();
        responseDto.setBalance(sender.getBalance());
        responseDto.setName(sender.getName());

        return responseDto;
    }

    @Override
    public List<TransactionResponseDto> getTransactionByAccount(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount(accountId,accountId);
        if (transactions == null) {
            return List.of(); // safe empty list
        }
            return transactions.stream().map(this::mapToDto).toList();
    }

    @Override
    public Page<TransactionResponseDto> getTransactionByAccountSize(Long accountId, int page, int pageSize) {
        //Pagenation logic and also now sorting applied
        Pageable pageable = PageRequest.of(page,pageSize,Sort.by("timestamp").descending());
        Page<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount(accountId,accountId,pageable);

        return transactions.map(this::mapToDto);
    }

    public TransactionResponseDto mapToDto(Transaction transaction){
        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setFromAccount(transaction.getFromAccount());
        responseDto.setToAccount(transaction.getToAccount());
        responseDto.setAmount(transaction.getAmount());
        responseDto.setType(transaction.getType());
        responseDto.setTimestamp(transaction.getTimestamp());

        return responseDto;
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(()-> new AccountNotFoundException("Account not found with id: " + id));
    }
}
