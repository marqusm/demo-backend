package com.marqusm.demobackendrestjavaspringboot.controller;

import com.marqusm.demobackendrestjavaspringboot.model.dto.AccountRequest;
import com.marqusm.demobackendrestjavaspringboot.model.dto.AccountResponse;
import com.marqusm.demobackendrestjavaspringboot.model.dto.LoginRequest;
import com.marqusm.demobackendrestjavaspringboot.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/** @author : Marko Mišković */
@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountsController {

  private final AccountService accountService;

  @PostMapping
  public AccountResponse createAccount(@Valid @RequestBody AccountRequest accountRequest) {
    return accountService.createAccount(accountRequest);
  }

  @PostMapping("/login")
  public void login(@Valid @RequestBody LoginRequest loginRequest) {
    accountService.login(loginRequest);
  }

  @PostMapping("/logout")
  public void logout() {
    accountService.logout();
  }
}
