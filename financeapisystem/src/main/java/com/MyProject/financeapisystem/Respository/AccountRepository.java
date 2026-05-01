package com.MyProject.financeapisystem.Respository;

import com.MyProject.financeapisystem.Modals.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
