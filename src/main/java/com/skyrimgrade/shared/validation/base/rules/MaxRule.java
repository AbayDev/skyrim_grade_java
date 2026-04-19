package com.skyrimgrade.shared.validation.base.rules;

public class MaxRule {
  
  public boolean validate(Integer value, Integer num) {
    return value <= num;
  }
}
