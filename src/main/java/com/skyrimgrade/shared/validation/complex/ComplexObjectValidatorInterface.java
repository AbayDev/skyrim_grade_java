package com.skyrimgrade.shared.validation.complex;

import java.util.function.Function;

import com.skyrimgrade.shared.validation.base.BaseValidationException;
import com.skyrimgrade.shared.validation.base.ValidateErrorText;

public interface ComplexObjectValidatorInterface<T> {

    <R> ComplexObjectValidatorInterface<T> required(Function<T, R> getter, String message);

    default <R> ComplexObjectValidatorInterface<T> required(Function<T, R> getter) {
        return required(getter, ValidateErrorText.REQUIRED.format());
    }

    <R> ComplexObjectValidatorInterface<T> maxLength(Function<T, R> getter, Integer length, String message);

    default <R> ComplexObjectValidatorInterface<T> maxLength(Function<T, R> getter, Integer length) {
        return maxLength(getter, length, ValidateErrorText.MAX_LENGTH.format(length.toString()));
    }

    <R> ComplexObjectValidatorInterface<T> minLength(Function<T, R> getter, Integer length, String message);

    default <R> ComplexObjectValidatorInterface<T> minLength(Function<T, R> getter, Integer length) {
        return minLength(getter, length, ValidateErrorText.MIN_LENGTH.format(length.toString()));
    }

    <R> ComplexObjectValidatorInterface<T> max(Function<T, R> getter, Integer num, String message);

    default <R> ComplexObjectValidatorInterface<T> max(Function<T, R> getter, Integer num) {
        return max(getter, num, ValidateErrorText.MAX.format(num.toString()));
    }

    <R> ComplexObjectValidatorInterface<T> min(Function<T, R> getter, Integer num, String message);

    default <R> ComplexObjectValidatorInterface<T> min(Function<T, R> getter, Integer num) {
        return min(getter, num, ValidateErrorText.MIN.format(num.toString()));
    }

    <R> ComplexObjectValidatorInterface<T> email(Function<T, R> getter, String message);

    default <R> ComplexObjectValidatorInterface<T> email(Function<T, R> getter) {
        return email(getter, ValidateErrorText.EMAIL.format());
    }

    <R> ComplexObjectValidatorInterface<T> enumValue(Function<T, R> getter, Class<? extends Enum<?>> enumClass, String message);

    default <R> ComplexObjectValidatorInterface<T> enumValue(Function<T, R> getter, Class<? extends Enum<?>> enumClass) {
        return enumValue(getter, enumClass, ValidateErrorText.ENUM.format());
    }

    <R> ComplexObjectValidatorInterface<T> pattern(Function<T, R> getter, String pattern, String message);

    default <R> ComplexObjectValidatorInterface<T> pattern(Function<T, R> getter, String pattern) {
        return pattern(getter, pattern, ValidateErrorText.PATTERN.format());
    }

    void throwIfErrored() throws BaseValidationException;
}
