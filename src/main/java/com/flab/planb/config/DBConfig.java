package com.flab.planb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
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

@EnableTransactionManagement
@PropertySource({"classpath:properties/datasource.properties"})
@Configuration
@MapperScan(basePackages = {"com.flab.planb.service.mapper"})
public class DBConfig {

  private final String driverClassName;
  private final String url;
  private final String userName;
  private final String password;
  private final int maxPoolSize;

  public DBConfig(@Value("${spring.datasource.driver-class-name}") String driverClassName,
      @Value("${spring.datasource.url}") String url,
      @Value("${spring.datasource.username}") String userName,
      @Value("${spring.datasource.password}") String password,
      @Value("${spring.datasource.max-pool-size}") int maxPoolSize) {
    this.driverClassName = driverClassName;
    this.url = url;
    this.userName = userName;
    this.password = password;
    this.maxPoolSize = maxPoolSize;
  }

  @Bean
  public HikariConfig hikariConfig() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(driverClassName);
    hikariConfig.setJdbcUrl(url);
    hikariConfig.setUsername(userName);
    hikariConfig.setPassword(password);
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
    sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/**/*.xml"));
    sessionFactory.setDataSource(dataSource);
    return sessionFactory.getObject();
  }

}
