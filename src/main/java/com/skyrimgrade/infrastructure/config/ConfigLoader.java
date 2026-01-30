package com.skyrimgrade.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration loader that reads from environment variables first,
 * then falls back to application.properties file.
 * 
 * Priority:
 * 1. Environment Variables (highest priority)
 * 2. application.properties (fallback)
 */
public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private final Properties properties;

    public ConfigLoader() {
        this("/application.properties");
    }

    public ConfigLoader(String propertiesFile) {
        properties = new Properties();
        try (InputStream input = getClass().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                logger.warn("Properties file not found: {}", propertiesFile);
            } else {
                properties.load(input);
                logger.info("Loaded configuration from: {}", propertiesFile);
            }
        } catch (IOException e) {
            logger.error("Failed to load properties file: {}", propertiesFile, e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    /**
     * Get configuration value with environment variable taking priority.
     * 
     * @param envKey Environment variable name (e.g., "DB_URL")
     * @param propertyKey Property file key (e.g., "db.url")
     * @return Configuration value
     */
    public String get(String envKey, String propertyKey) {
        // 1. Try environment variable first (highest priority)
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            logger.debug("Using env variable: {} = {}", envKey, maskSensitive(envKey, envValue));
            return envValue;
        }

        // 2. Fallback to properties file
        String propValue = properties.getProperty(propertyKey);
        if (propValue != null) {
            logger.debug("Using property: {} = {}", propertyKey, maskSensitive(propertyKey, propValue));
            return propValue;
        }

        // 3. Not found
        logger.warn("Configuration not found: env={}, property={}", envKey, propertyKey);
        return null;
    }

    /**
     * Get configuration value with default fallback.
     */
    public String get(String envKey, String propertyKey, String defaultValue) {
        String value = get(envKey, propertyKey);
        return value != null ? value : defaultValue;
    }

    /**
     * Get integer configuration value.
     */
    public int getInt(String envKey, String propertyKey, int defaultValue) {
        String value = get(envKey, propertyKey);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Invalid integer value for {}/{}: {}", envKey, propertyKey, value);
            return defaultValue;
        }
    }

    /**
     * Get boolean configuration value.
     */
    public boolean getBoolean(String envKey, String propertyKey, boolean defaultValue) {
        String value = get(envKey, propertyKey);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Mask sensitive values in logs (passwords, tokens, etc.)
     */
    private String maskSensitive(String key, String value) {
        if (key.toLowerCase().contains("password") || 
            key.toLowerCase().contains("secret") || 
            key.toLowerCase().contains("token")) {
            return "***";
        }
        return value;
    }

    /**
     * Get property directly from properties file (ignoring env vars)
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
