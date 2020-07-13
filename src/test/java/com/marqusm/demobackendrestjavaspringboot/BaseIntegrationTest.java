package com.marqusm.demobackendrestjavaspringboot;

import com.marqusm.demobackendrestjavaspringboot.model.dto.AccountRequest;
import com.marqusm.demobackendrestjavaspringboot.model.dto.LoginRequest;
import com.marqusm.demobackendrestjavaspringboot.repository.AccountRepository;
import io.restassured.http.ContentType;
import lombok.val;
import org.junit.ClassRule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;

/** @author : Marko Mišković */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

  @ClassRule public static TestContainer testContainer = TestContainer.getInstance();

  @Autowired private AccountRepository accountRepository;
  private static String token;

  private String getUserToken(String username, String password, String name) {
    if (token == null) {
      val request = AccountRequest.of(name, username, password);
      given().body(request).post("/accounts");
      token =
          given()
              .contentType(ContentType.JSON)
              .body(LoginRequest.of(username, password))
              .post("/accounts/login")
              .then()
              .extract()
              .cookie("auth_token");
    }
    return token;
  }

  private String getAdminToken() {
    if (token == null) {
      token =
          given()
              .contentType(ContentType.JSON)
              .body(LoginRequest.of("admin", "pass"))
              .post("/accounts/login")
              .then()
              .extract()
              .cookie("auth_token");
    }
    return token;
  }

  protected String getSellerToken() {
    return getUserToken("seller", "pass", "Seller");
  }

  protected String getBuyerToken() {
    return getUserToken("buyer", "pass", "Buyer");
  }
}
