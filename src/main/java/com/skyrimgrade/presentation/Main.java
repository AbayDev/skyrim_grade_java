package com.skyrimgrade.presentation;

import com.skyrimgrade.infrastructure.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for SkyrimGrade application.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting SkyrimGrade application...");

            // Load configuration
            AppConfig config = new AppConfig();
            logger.info("Configuration loaded: {}", config);

            // TODO: Initialize database connection (HikariCP)
            logger.info("Database URL: {}", config.getDatabaseUrl());
            logger.info("Connection pool size: {}", config.getDatabasePoolSize());

            // TODO: Run database migrations
            logger.info("Running database migrations...");

            // TODO: Initialize HTTP server (Jetty)
            logger.info("Starting HTTP server on {}:{}", config.getServerHost(), config.getServerPort());

            // TODO: Register REST controllers

            logger.info("SkyrimGrade {} started successfully in {} mode", 
                    config.getAppVersion(), 
                    config.getAppEnvironment());

            // Keep application running
            logger.info("Application is running. Press Ctrl+C to stop.");
            Thread.currentThread().join();

        } catch (Exception e) {
            logger.error("Failed to start application", e);
            System.exit(1);
        }
    }
}
