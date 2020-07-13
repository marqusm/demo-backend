package com.marqusm.demobackendrestjavaspringboot.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class PictureResponse {
  @NotNull private UUID id;
  @NotBlank private String name;
  @NotNull private List<String> hashtags;
}
