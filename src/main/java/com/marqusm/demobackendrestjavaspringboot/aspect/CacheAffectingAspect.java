package com.marqusm.demobackendrestjavaspringboot.aspect;

import com.marqusm.demobackendrestjavaspringboot.annotation.CacheAffecting;
import com.marqusm.demobackendrestjavaspringboot.cache.HashtagSuggestionCache;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/** @author : Marko Mišković */
@Slf4j
@AllArgsConstructor
@Aspect
@Order(1)
@Component
public class CacheAffectingAspect {

  private final HashtagSuggestionCache hashtagSuggestionCache;

  @Pointcut("@annotation(cacheAffecting)")
  public void callAt(CacheAffecting cacheAffecting) {}

  @SneakyThrows
  @After("callAt(cacheAffecting)")
  public void aspect(JoinPoint joinPoint, CacheAffecting cacheAffecting) {
    for (Object arg : joinPoint.getArgs()) {
      if (arg instanceof List && !((List) arg).isEmpty() && ((List) arg).get(0) instanceof String) {
        for (Object obj : (List) arg) {
          if (obj instanceof String) {
            var hashtag = (String) obj;
            hashtagSuggestionCache.invalidate(hashtag);
          }
        }
      }
    }
  }
}
