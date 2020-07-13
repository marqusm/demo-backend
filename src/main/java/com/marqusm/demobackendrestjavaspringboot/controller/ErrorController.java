package com.marqusm.demobackendrestjavaspringboot.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.marqusm.demobackendrestjavaspringboot.exception.DuplicateException;
import com.marqusm.demobackendrestjavaspringboot.model.dto.ErrorResponse;
import com.marqusm.demobackendrestjavaspringboot.service.ErrorService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** @author : Marko Mišković */
@Slf4j
@AllArgsConstructor
@ControllerAdvice
public class ErrorController {

  private final ErrorService errorService;

  @ExceptionHandler(DuplicateException.class)
  public ResponseEntity<ErrorResponse> processDuplicateException(
      Exception ex, HttpServletRequest request) {
    log.warn("processDuplicateException", ex);
    return errorService.createErrorResponse(HttpStatus.CONFLICT, ex, request);
  }

  @SneakyThrows
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> processHttpMessageNotReadableException(
      Exception ex, HttpServletRequest request) {
    log.warn("processHttpMessageNotReadableException", ex);
    if (ex.getCause() instanceof InvalidFormatException) {
      return errorService.createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex, request);
    } else {
      return errorService.createErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
    }
  }

  @SneakyThrows
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> processValidationException(
      Exception ex, HttpServletRequest request) {
    log.warn("processValidationException", ex);
    return errorService.createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex, request);
  }

  @SneakyThrows
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> processRuntimeException(
      RuntimeException ex, HttpServletRequest request, HttpServletResponse response) {
    log.error("processRuntimeException", ex);
    return errorService.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
  }
}
