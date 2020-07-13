package com.marqusm.demobackendrestjavaspringboot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Marko Mišković
 */
@Getter(value = AccessLevel.PACKAGE)
class TestData {
  @AllArgsConstructor(staticName = "of")
  @Getter(value = AccessLevel.PACKAGE)
  static class UserData {
    @Setter private String authToken;
    private final String username;
    private final String password;
    private final String name;
    private final String picturePath;
    private final String pictureName;
    private final String pictureHashtags;
  }

  private UserData admin =
      UserData.of(
          null,
          "admin",
          "c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec",
          "Administrator",
          null,
          null,
          null);
  private UserData userA =
      UserData.of(
          null,
          "user_a",
          "user_a",
          "User A",
          "/daimler.jpg",
          "Daimler image",
          "daimler,heybeach,newworld,thebest");
  private UserData userB =
      UserData.of(
          null,
          "user_b",
          "user_b",
          "User B",
          "/bmw.jpg",
          "BMW Concept",
          "bmw,heybeach,concept,werule");
  private UserData userC =
      UserData.of(
          null,
          "user_c",
          "user_c",
          "User C",
          "/audi.jpg",
          "Daimler image",
          "audi,heybeach,hybrid,dreamcar");
}
