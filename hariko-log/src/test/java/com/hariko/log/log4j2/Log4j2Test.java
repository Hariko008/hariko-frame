package com.hariko.log.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class Log4j2Test {
    private static Logger logger = LogManager.getLogger(Log4j2Test.class);

    @Test
    public void testQuick(){
        logger.info("hello, log4j2");
    }
}
