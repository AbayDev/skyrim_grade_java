package com.skyrimgrade.infrastructure.http;

import java.util.List;

import com.skyrimgrade.domain.exception.ValidationException;
import com.skyrimgrade.shared.validation.annotation.AnnotationObjectValidatorInterface;
import com.skyrimgrade.shared.validation.annotation.AnnotationValidatorInterface;
import com.skyrimgrade.shared.validation.base.BaseValidationException;

public class HttpContextAnnotationValidator implements HttpContextValidatorInterface {

  private final AnnotationValidatorInterface annotationValidator;

  public HttpContextAnnotationValidator(AnnotationValidatorInterface annotationValidator) {
    this.annotationValidator = annotationValidator;
  }

  @Override
  public void validate(Object object) throws ValidationException {
    try {
      AnnotationObjectValidatorInterface annotationObjectValidator = annotationValidator.validateByObject(object);
      annotationObjectValidator.validate();
    } catch (BaseValidationException e) {
      List<com.skyrimgrade.domain.exception.FieldError> domainErrors = e.getErrors().stream()
          .map(err -> new com.skyrimgrade.domain.exception.FieldError(err.field(), err.message()))
          .toList();
      throw new ValidationException(domainErrors);
    } 
  }
}
