package com.quest.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AppContextListener implements ServletContextListener {
    private final Logger logger = LogManager.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Конфигурация приложения
        try {
            ConfigApplication configApplication = ServiceLocator.getService(ConfigApplication.class);
            configApplication.initApplication();
        } catch (Exception e) {
            logger.error("AppContextListener initialization failed", e);
            throw new RuntimeException(e);
        }
        logger.info("Context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
