package com.hariko.log.log4j;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;
import org.junit.Test;

public class Log4jTest {
    @Test
    public void testQuick(){
        BasicConfigurator.configure();
        // log4j 内置日志记录标志 可以查看加载配置的过程
        LogLog.setInternalDebugging(true);

        Logger logger = Logger.getLogger(Log4jTest.class);

        logger.info("hello log4j");
    }
}
