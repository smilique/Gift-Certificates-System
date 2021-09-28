package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public abstract class JdbcConfig {

    protected static final String DRIVER_CLASSNAME = "${spring.datasource.driver-class-name}";
    protected static final String URL = "${spring.datasource.url}";
    protected static final String USERNAME = "${spring.datasource.username}";
    protected static final String PASSWORD = "${spring.datasource.password}";
    protected static final String MAX_IDLE = "${spring.datasource.max-idle}";
    protected static final String MAX_TOTAL = "${spring.datasource.max-total}";
    protected static final String MAX_WAIT_TIME = "${spring.datasource.max-wait-time}";

    static DataSource getDataSource(String driverClassName, String url, String username, String password, int maxIdle, int maxTotal, Long maxWaitTime) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxTotal(maxTotal);
        dataSource.setMaxWaitMillis(maxWaitTime);
        return dataSource;
    }
}
