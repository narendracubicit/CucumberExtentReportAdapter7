package com.narendra.automation;

import org.apache.log4j.Logger;

public interface Log4j {
    default Logger logger() {
        return Logger.getLogger(getClass());
    }
}
