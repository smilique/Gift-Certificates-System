package com.epam.esm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
@PropertySource("classpath:application_prod.properties")
public class ProdJdbcConfig extends JdbcConfig{

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
        return getDataSource(driverClassName, url, username, password, maxIdle, maxTotal, maxWaitTime);
    }

    @Bean
    public JdbcTemplate setDataSource() {
        return new JdbcTemplate(createDataSource());
    }
}
