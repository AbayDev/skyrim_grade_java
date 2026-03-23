package com.skyrimgrade.infrastructure.migration;

public class MigrationException extends RuntimeException {
  public MigrationException(String message, Throwable cause) {
    super(message, cause);
  }
}
