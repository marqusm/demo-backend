package com.marqusm.demobackendrestjavaspringboot.model.dto;

import com.marqusm.demobackendrestjavaspringboot.enumeration.PictureDisplayableStatus;
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
public class PictureDisplayableStatusRequest {
  @NotNull private UUID pictureId;
  @NotNull private PictureDisplayableStatus displayableStatus;
}
