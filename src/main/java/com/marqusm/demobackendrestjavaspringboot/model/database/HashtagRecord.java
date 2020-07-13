package com.marqusm.demobackendrestjavaspringboot.model.database;

import com.marqusm.demobackendrestjavaspringboot.annotation.Id;
import com.marqusm.demobackendrestjavaspringboot.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@Getter
@Table("hashtag")
public class HashtagRecord {
  @Id UUID id;
  String name;
}
