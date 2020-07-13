package com.marqusm.demobackendrestjavaspringboot.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class HashtagCollectionResponse {
  @NotNull private List<String> items;
}
