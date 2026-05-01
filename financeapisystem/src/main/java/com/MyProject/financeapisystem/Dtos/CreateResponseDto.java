package com.MyProject.financeapisystem.Dtos;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateResponseDto {

 //   private Long id;
    private String name;
    private BigDecimal balance;

}
