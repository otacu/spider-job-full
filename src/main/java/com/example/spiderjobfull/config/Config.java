package com.example.spiderjobfull.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Value("${my.jdbc.driver}")
    private String JDBC_DRIVER;
    @Value("${my.db.url}")
    private String DB_URL;
    @Value("${my.db.username}")
    private String USER;
    @Value("${my.db.password}")
    private String PASS;
    @Value("${my.webdriver.path}")
    private String WEB_DRIVER_PATH;
    @Value("${my.download.start.date}")
    private String START_DATE;
    @Value("${my.download.end.date}")
    private String END_DATE;

    /**
     * @return JDBC_DRIVER
     */
    public String getJDBC_DRIVER() {
        return JDBC_DRIVER;
    }

    /**
     * @return USER
     */
    public String getUSER() {
        return USER;
    }

    /**
     * @return PASS
     */
    public String getPASS() {
        return PASS;
    }

    /**
     * @return DB_URL
     */
    public String getDB_URL() {
        return DB_URL;
    }

    /**
     * @return WEB_DRIVER_PATH
     */
    public String getWEB_DRIVER_PATH() {
        return WEB_DRIVER_PATH;
    }

    /**
     * @return START_DATE
     */
    public String getSTART_DATE() {
        return START_DATE;
    }

    /**
     * @return END_DATE
     */
    public String getEND_DATE() {
        return END_DATE;
    }
}
