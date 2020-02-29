package com.hariko.log.jul;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.*;

public class JulTest {

    @Test
    public void testQuick(){
        Logger logger = Logger.getLogger("com.hariko.log.jul.JulTest");

        logger.info("hello, jul");

        logger.log(Level.INFO, "hello, {0}, {1}", new Object[]{"jul", "welcome"});
    }

    @Test
    public void testLevel(){
        Logger logger = Logger.getLogger("com.hariko.log.jul.JulTest");
        // 默认日志级别为 info
        // 创建自己的 handler 实现日志自定义级别
        ConsoleHandler consoleHandler = new ConsoleHandler();

        SimpleFormatter simpleFormatter = new SimpleFormatter();

        consoleHandler.setFormatter(simpleFormatter);
        consoleHandler.setLevel(Level.ALL);

        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        logger.severe("severe");
        logger.warning("warning");
        logger.info("info");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");
    }

    @Test
    public void testReadConf() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("logging.properties");

        LogManager logManager = LogManager.getLogManager();

        logManager.readConfiguration(inputStream);

        Logger logger = Logger.getLogger("com.hariko.log.jul.JulTest");

        logger.severe("severe");
        logger.warning("warning");
        logger.info("info");
        logger.fine("fine");
        logger.finer("finer");
        logger.finest("finest");
    }
}
