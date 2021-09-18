package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class JdbcConfig {

    private static final String DRIVER_CLASSNAME = "${spring.datasource.driver-class-name}";
    private static final String URL = "${spring.datasource.url}";
    private static final String USERNAME = "${spring.datasource.username}";
    private static final String PASSWORD = "${spring.datasource.password}";
    private static final String MAX_IDLE = "${spring.datasource.max-idle}";
    private static final String MAX_TOTAL = "${spring.datasource.max-total}";
    private static final String MAX_WAIT_TIME = "${spring.datasource.max-wait-time}";

    @Value(DRIVER_CLASSNAME)
    private String driverClassName;
    @Value(URL)
    private String url;
    @Value(USERNAME)
    private String username;
    @Value(PASSWORD)
    private String password;
    @Value(MAX_IDLE)
    private int maxIdle;
    @Value(MAX_TOTAL)
    private int maxTotal;
    @Value(MAX_WAIT_TIME)
    private Long maxWaitTime;

    @Bean
    public DataSource createDataSource() {
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

    @Bean
    public JdbcTemplate setDataSource() {
        return new JdbcTemplate(createDataSource());
    }
}
