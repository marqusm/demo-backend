package com.marqusm.demobackendrestjavaspringboot.exception;

public class DuplicateException extends RuntimeException {
  public DuplicateException(String message) {
    super(message);
  }
}
