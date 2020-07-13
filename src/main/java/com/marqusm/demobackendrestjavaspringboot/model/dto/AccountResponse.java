package com.marqusm.demobackendrestjavaspringboot.model.dto;

import com.marqusm.demobackendrestjavaspringboot.enumeration.AccountRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class AccountResponse {
  @NotNull private UUID id;
  @NotBlank private String name;
  @NotBlank private String username;
  @NotNull private AccountRole role;
}
