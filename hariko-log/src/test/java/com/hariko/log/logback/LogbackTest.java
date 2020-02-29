package com.hariko.log.logback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {
    private static Logger logger = LoggerFactory.getLogger(LogbackTest.class);
    @Test
    public void testQuick(){
        logger.info("hello, logback");
    }
}
