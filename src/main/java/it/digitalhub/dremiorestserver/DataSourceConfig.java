package it.digitalhub.dremiorestserver;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.arrow.driver.jdbc.ArrowFlightJdbcDriver");
        dataSource.setUrl("jdbc:arrow-flight-sql://localhost:32010/?useEncryption=false&disableCertificateVerification=true&user=dremio&password=dremio123");
        return dataSource;
    }
}