package com.marqusm.demobackendrestjavaspringboot.model.database;

import com.marqusm.demobackendrestjavaspringboot.annotation.Id;
import com.marqusm.demobackendrestjavaspringboot.annotation.Table;
import com.marqusm.demobackendrestjavaspringboot.enumeration.PictureDisplayableStatus;
import com.marqusm.demobackendrestjavaspringboot.enumeration.PicturePurchasableStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@Getter
@Table("picture")
public class PictureRecord {
  @Id UUID id;
  UUID accountId;
  UUID fileId;
  String name;
  PictureDisplayableStatus displayable_status;
  PicturePurchasableStatus purchasable_status;
}
