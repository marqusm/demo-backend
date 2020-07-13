package com.marqusm.demobackendrestjavaspringboot.model.database;

import com.marqusm.demobackendrestjavaspringboot.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@Getter
@Table("picture_x_hashtag")
public class PictureToHashtagRecord {
  UUID pictureId;
  UUID hashtagId;
}
