package com.skyrimgrade.shared.validation.complex;

public interface ComplexValidatorInterface {
  ComplexObjectValidatorInterface<Object> validateByObject(Object obj);
}
