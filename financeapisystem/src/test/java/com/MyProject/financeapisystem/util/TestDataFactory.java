package com.MyProject.financeapisystem.util;

import com.MyProject.financeapisystem.Dtos.CreateRequestDto;

import java.math.BigDecimal;

public class TestDataFactory {

    public static CreateRequestDto createAccount(String name, double balance){
        CreateRequestDto dto = new CreateRequestDto();
        dto.setName(name);
        dto.setBalance(BigDecimal.valueOf(balance));

        return dto;
    }
}
