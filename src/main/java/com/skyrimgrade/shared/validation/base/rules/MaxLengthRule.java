package com.skyrimgrade.shared.validation.base.rules;

public class MaxLengthRule {
  
  public boolean validate(String value, Integer length) {
    return value.length() <= length;
  }
}
