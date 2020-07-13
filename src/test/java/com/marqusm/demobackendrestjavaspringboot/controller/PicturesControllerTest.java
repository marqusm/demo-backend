package com.marqusm.demobackendrestjavaspringboot.controller;

import com.marqusm.demobackendrestjavaspringboot.BaseIntegrationTest;
import com.marqusm.demobackendrestjavaspringboot.model.dto.PictureCollectionResponse;
import com.marqusm.demobackendrestjavaspringboot.model.dto.PictureResponse;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PicturesControllerTest extends BaseIntegrationTest {

  @SneakyThrows
  private PictureResponse createPicture(
      String token, String fileName, String name, String hashtags) {
    val requestFile = new ClassPathResource(fileName).getFile();
    val requestParams = Map.of("name", name, "hashtags", hashtags);
    return given()
        .contentType(ContentType.MULTIPART_FORM_DATA.getMimeType())
        .cookie("auth_token", token)
        .multiPart("file", requestFile, "image/jpeg")
        .formParams(requestParams) // requestParamsMap here.
        .post("/pictures")
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .as(PictureResponse.class);
  }

  @SneakyThrows
  @Test
  void createPicture() {
    val requestFile = new ClassPathResource("caucasus.jpg").getFile();
    val requestParams = Map.of("name", "The Caucasus", "hashtags", "nature,mountain");

    val expectedResponse =
        PictureResponse.of(
            null,
            requestParams.get("name"),
            Arrays.asList(requestParams.get("hashtags").split(",")));

    val response =
        given()
            .contentType(ContentType.MULTIPART_FORM_DATA.getMimeType())
            .cookie("auth_token", getSellerToken())
            .multiPart("file", requestFile, "image/jpeg")
            .formParams(requestParams) // requestParamsMap here.
            .post("/pictures")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(PictureResponse.class);

    expectedResponse.setId(response.getId());
    assertThat(response, equalTo(expectedResponse));
  }

  @Test
  void readAllPictures_nonApproved() {
    createPicture(getSellerToken(), "daisy.jpg", "Daisy", "nature,flower");
    createPicture(getSellerToken(), "jellyfish.jpg", "Jellyfish", "nature,sea");

    val response =
        given()
            .cookie("auth_token", getSellerToken())
            .get("/pictures")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(PictureCollectionResponse.class);

    assertThat(response.getItems().size(), equalTo(0));
  }
}
