package com.marqusm.demobackendrestjavaspringboot.model.database;

import com.marqusm.demobackendrestjavaspringboot.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@Getter
@Table("hashtag_popularity")
public class HashtagPopularityRecord {
  String name;
  Long count;
}
