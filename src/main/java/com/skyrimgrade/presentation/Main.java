package com.skyrimgrade.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skyrimgrade.infrastructure.config.AppConfig;
import com.skyrimgrade.infrastructure.config.ConfigLoader;
import com.skyrimgrade.infrastructure.config.ControllerRegistrar;
import com.skyrimgrade.infrastructure.container.DIContainer;
import com.skyrimgrade.infrastructure.http.JettyServer;
import com.skyrimgrade.infrastructure.http.Router;
import com.skyrimgrade.infrastructure.http.RouterScanner;
import com.skyrimgrade.infrastructure.migration.FlywayMigrationRunner;
import com.skyrimgrade.infrastructure.persistence.DatabaseConnectionManager;
import com.skyrimgrade.presentation.rest.controllers.AuthController;
import com.skyrimgrade.presentation.rest.controllers.HealthController;
import com.skyrimgrade.presentation.rest.controllers.UserContextController;

/**
 * Main entry point for SkyrimGrade application.
 *
 * Composition Root — единственное место где собирается граф зависимостей.
 * Только здесь знают какие реализации используются.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting SkyrimGrade application...");

            // ═══════════════════════════════════════════════════════════
            // ФАЗА 1: Регистрация — описываем граф, объекты ещё не созданы
            // ═══════════════════════════════════════════════════════════
            DIContainer container = new DIContainer();

            container
                    .register(ConfigLoader.class)
                    .register(AppConfig.class)
                    .register(DatabaseConnectionManager.class)
                    .register(FlywayMigrationRunner.class);

            // TODO: по мере роста проекта добавлять сюда:
            // .register(UserRepository.class, PostgresUserRepository.class)
            // .register(UserService.class)
            // .register(UserController.class)
            // .register(JettyServer.class)
            // ═══════════════════════════════════════════════════════════
            // ФАЗА 2: Инициализация — контейнер рекурсивно создаёт весь граф
            // ═══════════════════════════════════════════════════════════
            AppConfig config = container.get(AppConfig.class);
            logger.info("Configuration loaded: {}", config);

            logger.info("Initializing database connection pool...");
            DatabaseConnectionManager dbManager = container.get(DatabaseConnectionManager.class);

            // ═══════════════════════════════════════════════════════════
            // ФАЗА 3: Проверки и запуск
            // ═══════════════════════════════════════════════════════════
            if (!dbManager.isHealthy()) {
                logger.error("Database health check failed! Cannot start application.");
                System.exit(1);
            }
            logger.info("Database connection successful!");

            DatabaseConnectionManager.PoolStats stats = dbManager.getPoolStats();
            logger.info("Connection pool initialized: {}", stats);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down application...");
                dbManager.shutdown();
                logger.info("Application shutdown complete");
            }));

            FlywayMigrationRunner flywayMigrationRunner = container.get(FlywayMigrationRunner.class);
            flywayMigrationRunner.migrate();

            Router router = new Router();
            RouterScanner routerScanner = new RouterScanner(router);
            ControllerRegistrar contollerRegistrar = new ControllerRegistrar(container, routerScanner);
            contollerRegistrar.register(
                    AuthController.class,
                    UserContextController.class,
                    HealthController.class
            );

            JettyServer server = new JettyServer(config, router);
            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    server.stop();
                } catch (Exception e) {
                    logger.error("Error stopping server", e);
                }
            }));

            logger.info("SkyrimGrade {} started successfully in {} mode",
                    config.getAppVersion(),
                    config.getAppEnvironment());

            logger.info("Application is running. Press Ctrl+C to stop.");
            Thread.currentThread().join();

        } catch (Exception e) {
            logger.error("Failed to start application", e);
            System.exit(1);
        }
    }
}
