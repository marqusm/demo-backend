package com.marqusm.demobackendrestjavaspringboot.service;

import com.marqusm.demobackendrestjavaspringboot.annotation.Transactional;
import com.marqusm.demobackendrestjavaspringboot.configuration.AppConfig;
import com.marqusm.demobackendrestjavaspringboot.enumeration.AccountRole;
import com.marqusm.demobackendrestjavaspringboot.model.database.AccountRecord;
import com.marqusm.demobackendrestjavaspringboot.model.dto.AccountRequest;
import com.marqusm.demobackendrestjavaspringboot.model.dto.AccountResponse;
import com.marqusm.demobackendrestjavaspringboot.model.dto.LoginRequest;
import com.marqusm.demobackendrestjavaspringboot.repository.AccountRepository;
import com.marqusm.demobackendrestjavaspringboot.util.CookieUtil;
import com.marqusm.demobackendrestjavaspringboot.util.ModelMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/** @author : Marko Mišković */
@AllArgsConstructor
@Service
public class AccountService {

  private final HttpServletResponse httpResponse;
  private final AppConfig appConfig;
  private final AccountRepository accountRepository;

  @Transactional
  public AccountResponse createAccount(AccountRequest accountRequest) {
    var accountRecord =
        AccountRecord.of(
            null,
            accountRequest.getUsername(),
            accountRequest.getPassword(),
            AccountRole.GENERAL,
            accountRequest.getName());
    accountRepository.create(accountRecord);
    return ModelMapper.toAccountResponse(accountRecord);
  }

  @Transactional
  public void login(LoginRequest loginRequest) {
    val accountRecord = findAccountRecord(loginRequest.getUsername(), loginRequest.getPassword());
    val jwtToken = createJwtToken(accountRecord.getId().toString(), accountRecord.getRole().name());
    httpResponse.addCookie(
        CookieUtil.createAuthCookie(jwtToken, appConfig.getSecurityTokenLifetime()));
  }

  public void logout() {
    Cookie cookie = CookieUtil.createAuthCookie(null, 0);
    httpResponse.addCookie(cookie);
  }

  private AccountRecord findAccountRecord(String username, String password) {
    return accountRepository
        .findOneByExample(AccountRecord.of(null, username, password, null, null))
        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found"));
  }

  private String createJwtToken(String id, String role) {
    return Jwts.builder()
        .setSubject(id)
        .claim("role", role)
        .setIssuedAt(new Date())
        .setExpiration(
            new Date(new Date().getTime() + 1_000 * appConfig.getSecurityTokenLifetime()))
        .signWith(SignatureAlgorithm.HS256, appConfig.getSecurityJwtSecret())
        .compact();
  }
}
