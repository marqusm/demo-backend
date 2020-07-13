package com.marqusm.demobackendrestjavaspringboot.util;

import com.marqusm.demobackendrestjavaspringboot.model.database.AccountRecord;
import com.marqusm.demobackendrestjavaspringboot.model.database.HashtagRecord;
import com.marqusm.demobackendrestjavaspringboot.model.database.PictureRecord;
import com.marqusm.demobackendrestjavaspringboot.model.dto.AccountResponse;
import com.marqusm.demobackendrestjavaspringboot.model.dto.PictureResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelMapper {
  public static AccountResponse toAccountResponse(AccountRecord accountRecord) {
    return AccountResponse.of(
        accountRecord.getId(),
        accountRecord.getName(),
        accountRecord.getUsername(),
        accountRecord.getRole());
  }

  public static PictureResponse toPictureResponse(
      PictureRecord pictureRecord, List<HashtagRecord> hashtagRecords) {
    return PictureResponse.of(
        pictureRecord.getId(),
        pictureRecord.getName(),
        Optional.ofNullable(hashtagRecords)
            .map(hr -> hr.stream().map(HashtagRecord::getName).collect(Collectors.toList()))
            .orElse(null));
  }
}
