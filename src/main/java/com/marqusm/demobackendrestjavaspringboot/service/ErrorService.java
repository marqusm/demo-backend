package com.marqusm.demobackendrestjavaspringboot.service;

import com.marqusm.demobackendrestjavaspringboot.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ErrorService {
  public ResponseEntity<ErrorResponse> createErrorResponse(
      HttpStatus httpStatus, List<String> messages, HttpServletRequest request) {
    return new ResponseEntity<>(
        ErrorResponse.of(
            OffsetDateTime.now(),
            httpStatus.value(),
            httpStatus.name(),
            messages,
            request.getRequestURI()),
        httpStatus);
  }

  public ResponseEntity<ErrorResponse> createErrorResponse(
      HttpStatus httpStatus, Exception ex, HttpServletRequest request) {
    return createErrorResponse(httpStatus, Collections.singletonList(ex.getMessage()), request);
  }
}
