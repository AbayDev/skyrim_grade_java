package com.skyrimgrade.shared.validation.base.rules;

public class MinRule {

    public boolean validate(Integer value, Integer num) {
        return value >= num;
    }
}
