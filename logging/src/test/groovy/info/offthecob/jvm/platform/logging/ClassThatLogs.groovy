package info.offthecob.jvm.platform.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC

class ClassThatLogs {
    private static final Logger logger = LoggerFactory.getLogger(ClassThatLogs.class);

    static void sayHi(String username) {
        logger.info("Hi, {}!", username);
    }

    static void throwStack(String message) {
        logger.error(message, new RuntimeException(message))
    }

    static void mdc(String key, String value, String message) {
        MDC.put(key, value)
        logger.info(message)
        MDC.remove(key)
    }
}
