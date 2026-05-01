package com.MyProject.financeapisystem.Modals;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private String type;  // (DEPOSIT, WITHDRAW, TRANSFER)
    private LocalDateTime timestamp;
}
