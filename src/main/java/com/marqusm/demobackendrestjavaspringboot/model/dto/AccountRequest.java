package com.marqusm.demobackendrestjavaspringboot.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class AccountRequest {
  @NotBlank(message = "Name is mandatory")
  private String name;

  @NotBlank(message = "Username is mandatory")
  private String username;

  @NotBlank(message = "Password is mandatory")
  private String password;
}
