package com.MyProject.financeapisystem.Respository;


import com.MyProject.financeapisystem.Modals.Users;
//import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRespository extends JpaRepository<Users,Long> {

    Optional<Users> findByUsername(String username);
}
