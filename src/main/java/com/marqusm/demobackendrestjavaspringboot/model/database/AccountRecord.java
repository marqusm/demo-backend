package com.marqusm.demobackendrestjavaspringboot.model.database;

import com.marqusm.demobackendrestjavaspringboot.annotation.Id;
import com.marqusm.demobackendrestjavaspringboot.annotation.Table;
import com.marqusm.demobackendrestjavaspringboot.enumeration.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@Getter
@Table("account")
public class AccountRecord {
  @Id UUID id;
  String username;
  String passwordHash;
  AccountRole role;
  String name;
}
