package com.skyrimgrade.shared.validation.base.rules;

import java.util.regex.Pattern;

public class PatternRule {

    public boolean validate(String value, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        return pattern.matcher(value).matches();
    }
}
