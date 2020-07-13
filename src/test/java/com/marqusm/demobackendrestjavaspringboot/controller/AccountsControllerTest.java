package com.marqusm.demobackendrestjavaspringboot.controller;

import com.marqusm.demobackendrestjavaspringboot.BaseIntegrationTest;
import com.marqusm.demobackendrestjavaspringboot.enumeration.AccountRole;
import com.marqusm.demobackendrestjavaspringboot.model.dto.AccountRequest;
import com.marqusm.demobackendrestjavaspringboot.model.dto.AccountResponse;
import com.marqusm.demobackendrestjavaspringboot.model.dto.ErrorResponse;
import com.marqusm.demobackendrestjavaspringboot.model.dto.LoginRequest;
import io.restassured.http.ContentType;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author : Marko Mišković
 */
class AccountsControllerTest extends BaseIntegrationTest {

  @Test
  void createAccount() {
    val request = AccountRequest.of("Test Account 1", "test_account_1", "pass");
    val expectedResponse =
        AccountResponse.of(null, request.getName(), request.getUsername(), AccountRole.GENERAL);

    val response =
        given()
            .body(request)
            .post("/accounts")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(AccountResponse.class);

    expectedResponse.setId(response.getId());
    assertThat(response, equalTo(expectedResponse));
  }

  @Test
  void createAccount_invalidRequest() {
    val expectedResponse =
        ErrorResponse.of(
            null,
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.name(),
            Collections.emptyList(),
            "/accounts");

    val response =
        given()
            .contentType(ContentType.JSON)
            .body(AccountRequest.of("Test Account 1", "test_account_1", null))
            .post("/accounts")
            .then()
            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .extract()
            .as(ErrorResponse.class);

    expectedResponse.setTimestamp(response.getTimestamp());
    expectedResponse.setMessages(response.getMessages());
    assertThat(response, equalTo(expectedResponse));
  }

  @Test
  void login() {
    val request = AccountRequest.of("Test Read Account 1", "test_read_account_1", "pass");

    given()
        .contentType(ContentType.JSON)
        .body(request)
        .post("/accounts")
        .then()
        .extract()
        .as(AccountResponse.class);

    given()
        .contentType(ContentType.JSON)
        .body(LoginRequest.of("test_read_account_1", "pass"))
        .post("/accounts/login")
        .then()
        .statusCode(HttpStatus.OK.value())
        .cookie("auth_token", notNullValue());
  }
}
