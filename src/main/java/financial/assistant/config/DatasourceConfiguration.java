package financial.assistant.config;

import financial.assistant.utils.DatabaseFileLocationDeterminer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "datasource.h2")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "h2EntityManagerFactory", basePackages = {
        "financial.assistant.repository" }, transactionManagerRef = "h2TransactionManager")
public class DatasourceConfiguration {

    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private String initializationMode;
    private String platform;
    private String name;
    private String dialect;
    private String mode;

    @Primary
    @Bean(name = "h2Datasource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(getDriverClassName());
        dataSource.setUrl(getUrl().replace("{{fileLocation}}", DatabaseFileLocationDeterminer.determineDatabaseFileLocation()));
        dataSource.setUsername(getUsername());
        dataSource.setPassword(getPassword());
        return dataSource;
    }

    @Primary
    @Bean(name = "h2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("h2Datasource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("financial.assistant.entity")
                .persistenceUnit("h2").properties(properties()).build();
    }

    @Primary
    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("h2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    public Map<String, String> properties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", getDialect());
        properties.put("hibernate.hbm2ddl.auto", getMode());
        properties.put("spring.datasource.initialization-mode", getInitializationMode());
        return properties;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitializationMode() {
        return initializationMode;
    }

    public void setInitializationMode(String initializationMode) {
        this.initializationMode = initializationMode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
