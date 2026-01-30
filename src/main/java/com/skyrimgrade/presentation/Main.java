package com.skyrimgrade.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyrimgrade.infrastructure.config.AppConfig;
import com.skyrimgrade.infrastructure.persistence.DatabaseConnectionManager;

/**
 * Main entry point for SkyrimGrade application.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting SkyrimGrade application...");

            // загружаем конфигурацию
            AppConfig config = new AppConfig();
            logger.info("Configuration loaded: {}", config);

            // инициализируем connection pool
            logger.info("Initializing database connection pool...");
            DatabaseConnectionManager dbManager = DatabaseConnectionManager.getInstance(config);

            // проверяем подключение к БД
            if (!dbManager.isHealthy()) {
                logger.error("Database health check failed! Cannot start application.");
                System.exit(1);
            }
            logger.info("Database connection successful!");

            // показываем статистику пула
            DatabaseConnectionManager.PoolStats stats = dbManager.getPoolStats();
            logger.info("Connect  pool initialized: {}", stats);

            // добавляем shutdown hook для graceful завершения
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shuttin down application...");
                try {
                    dbManager.shutdown();
                    logger.info("Application shutdown complete");
                } catch (Exception e) {
                    logger.error("Error during shutdown", e);
                }
            }));

            // TODO: Запустить миграции БД (Flyway)
            logger.info("Running database migrations...");

            // TODO: Инициализировать HTTP server (Jetty)
            logger.info("Starting HTTP server on {}:{}", config.getServerHost(), config.getServerPort());

            // TODO: Зарегистрировать REST контроллеры
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
