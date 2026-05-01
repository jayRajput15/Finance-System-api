package com.MyProject.financeapisystem.Controller;

import com.MyProject.financeapisystem.Dtos.LoginRequestDto;
import com.MyProject.financeapisystem.Dtos.LoginResponseDto;
import com.MyProject.financeapisystem.Exception.AuthenticationFailedException;
import com.MyProject.financeapisystem.Modals.Users;
import com.MyProject.financeapisystem.Respository.UserRespository;
import com.MyProject.financeapisystem.Security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        Users user = userRespository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(()-> new AuthenticationFailedException("Invalid username or password"));

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            throw new AuthenticationFailedException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponseDto(token);
    }
}
