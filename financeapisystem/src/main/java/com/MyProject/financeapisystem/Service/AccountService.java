package com.MyProject.financeapisystem.Service;


import com.MyProject.financeapisystem.Dtos.CreateRequestDto;
import com.MyProject.financeapisystem.Dtos.CreateResponseDto;
import com.MyProject.financeapisystem.Dtos.TransactionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {


    CreateResponseDto getAccount(Long id);

    CreateResponseDto createAccount(CreateRequestDto createRequestDto);

    CreateResponseDto updateAccountDetails(Long id, CreateRequestDto createRequestDto);

    void deleteAccount(Long id);

    CreateResponseDto withdraw(Long id, BigDecimal amount);

    CreateResponseDto deposit(Long id, BigDecimal money);

    CreateResponseDto transfer(Long fromId, Long toId, BigDecimal money);

    List<TransactionResponseDto> getTransactionByAccount(Long accountId);

    Page<TransactionResponseDto> getTransactionByAccountSize(Long accountId, int page, int pageSize);
}
