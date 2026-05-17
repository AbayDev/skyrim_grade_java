package com.skyrimgrade.infrastructure.http;

import com.skyrimgrade.domain.exception.ValidationException;

public interface HttpContextValidatorInterface {
  void validate(Object object) throws ValidationException;
}
