package com.chariotsolutions.rabbitmq.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleStringBean {
    private static final Logger logger = LoggerFactory.getLogger(SimpleStringBean.class);

    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void printMessage(String m) {
        logger.debug("HEY... Received message: {}", m);
    }

}
