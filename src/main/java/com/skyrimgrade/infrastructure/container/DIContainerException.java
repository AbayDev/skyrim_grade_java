package com.skyrimgrade.infrastructure.container;

public class DIContainerException extends Exception {

    public DIContainerException(String message) {
        super("[DIContainerExceptin] " + message);
    }

}
