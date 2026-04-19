package com.skyrimgrade.shared.validation.base;

public interface BaseValidatorInterface {

    <T> boolean required(T value);

    boolean maxLength(String value, Integer length);

    boolean minLength(String value, Integer length);

    boolean max(Integer value, Integer num);

    boolean min(Integer value, Integer num);

    boolean enums(Object value, Class<? extends Enum<?>> enumClass);

    boolean email(String value);

    boolean pattern(String value, String pattern);
}
