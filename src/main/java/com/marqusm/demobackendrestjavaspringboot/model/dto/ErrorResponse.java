package com.marqusm.demobackendrestjavaspringboot.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class ErrorResponse {
  @NotNull private OffsetDateTime timestamp;
  @NotNull private Integer status;
  @NotBlank private String error;
  @NotNull List<String> messages;
  @NotBlank String path;
}
