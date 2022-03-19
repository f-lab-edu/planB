package com.flab.planb.config;

import com.flab.planb.type.DayTypeHandler;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:properties/datasource.properties"})
@MapperScan(basePackages = {"com.flab.planb.mapper"})
public class DBConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.max-pool-size}")
    private int maxPoolSize;
    @Value("${jasypt.encryptor.password}")
    private String jasyptEncryptKey;
    @Value("${jasypt.encryptor.algorithm}")
    private String jasyptAlgorithm;
    @Value("${jasypt.encryptor.iv-generator-class-name}")
    private String jasyptIvGeneratorClassName;
    @Value("${jasypt.encryptor.key-obtention-iterations}")
    private int jasyptKeyObtentionIterations;
    @Value("${jasypt.encryptor.pool-size}")
    private int jasyptPoolSize;

    @Bean
    public HikariConfig hikariConfig(PBEStringEncryptor pbeStringEncryptor) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(pbeStringEncryptor.decrypt(userName));
        hikariConfig.setPassword(pbeStringEncryptor.decrypt(password));
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("planbHikariCP");
        return hikariConfig;
    }

    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/**/*.xml"));
        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeHandlers(new DayTypeHandler());
        return sessionFactory.getObject();
    }

    @Bean
    public PBEStringEncryptor pooledPBEStringEncryptor() {
        EnvironmentStringPBEConfig pbeConfig = new EnvironmentStringPBEConfig();
        pbeConfig.setPasswordEnvName(jasyptEncryptKey);
        pbeConfig.setAlgorithm(jasyptAlgorithm);
        pbeConfig.setKeyObtentionIterations(jasyptKeyObtentionIterations);
        pbeConfig.setIvGeneratorClassName(jasyptIvGeneratorClassName);
        pbeConfig.setPoolSize(jasyptPoolSize);
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        pooledPBEStringEncryptor.setConfig(pbeConfig);
        return pooledPBEStringEncryptor;
    }
}
