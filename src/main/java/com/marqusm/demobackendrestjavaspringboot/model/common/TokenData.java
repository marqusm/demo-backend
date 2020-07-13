package com.marqusm.demobackendrestjavaspringboot.model.common;

import com.marqusm.demobackendrestjavaspringboot.enumeration.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
public class TokenData {
  private UUID accountId;
  private AccountRole accountRole;
}
