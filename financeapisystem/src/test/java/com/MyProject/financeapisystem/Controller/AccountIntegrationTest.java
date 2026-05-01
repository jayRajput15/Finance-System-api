package com.MyProject.financeapisystem.Controller;

import com.MyProject.financeapisystem.util.TestDataFactory;
import com.MyProject.financeapisystem.Security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private JwtUtil jwtUtil;

    @Test
    void testCreateAccount() throws Exception{

        String json = objectMapper.writeValueAsString(TestDataFactory.createAccount("Jay",1000));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtUtil.generateToken("test-user"))
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jay"));

    }
}
