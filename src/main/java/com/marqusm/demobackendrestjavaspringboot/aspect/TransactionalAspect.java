package com.marqusm.demobackendrestjavaspringboot.aspect;

import com.marqusm.demobackendrestjavaspringboot.annotation.Transactional;
import com.marqusm.demobackendrestjavaspringboot.repository.connection.DbConnectionManager;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** @author : Marko Mišković */
@Slf4j
@AllArgsConstructor
@Aspect
@Order(0)
@Component
public class TransactionalAspect {

  private final DbConnectionManager dbConnectionManager;

  @Pointcut("@annotation(transactional)")
  public void callAt(Transactional transactional) {}

  @SneakyThrows
  @Around("callAt(transactional)")
  public Object aspect(ProceedingJoinPoint joinPoint, Transactional transactional) {
    try {
      Object result = joinPoint.proceed();
      dbConnectionManager.commitAndCloseConnection();
      return result;
    } catch (Exception e) {
      dbConnectionManager.closeConnection();
      throw e;
    }
  }
}
