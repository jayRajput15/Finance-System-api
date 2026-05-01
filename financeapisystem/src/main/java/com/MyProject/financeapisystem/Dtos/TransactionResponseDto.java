package com.MyProject.financeapisystem.Dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponseDto {

    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private String type;
    private LocalDateTime timestamp;
}
