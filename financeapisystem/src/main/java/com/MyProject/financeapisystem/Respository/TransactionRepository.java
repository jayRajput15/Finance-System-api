package com.MyProject.financeapisystem.Respository;

import com.MyProject.financeapisystem.Dtos.TransactionResponseDto;
import com.MyProject.financeapisystem.Modals.Transaction;
//import org.hibernate.query.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.awt.print.Pageable;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByFromAccountOrToAccount(Long from, Long to);

    Page<Transaction> findByFromAccountOrToAccount(Long from , Long to, Pageable pageable);
}
