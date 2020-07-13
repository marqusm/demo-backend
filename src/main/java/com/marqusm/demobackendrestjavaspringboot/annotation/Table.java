package com.marqusm.demobackendrestjavaspringboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @author : Marko Mišković */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
  String value();
}
