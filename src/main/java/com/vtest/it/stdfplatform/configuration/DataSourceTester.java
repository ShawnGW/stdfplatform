package com.vtest.it.stdfplatform.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@MapperScan(basePackages = {"com.vtest.it.stdfplatform.dao.tester.TesterDao"},sqlSessionTemplateRef ="testerSqlSessionTemplate")
public class DataSourceTester {
    @Bean(value = "testerDataSource",initMethod = "init",destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource.druid.tester")
    public DruidDataSource DataSource() {
        DruidDataSource dataSource=new DruidDataSource();
        try {
            dataSource.setFilters("stat,wall,log4j");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
    @Bean(value = "testerSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("testerDataSource") DruidDataSource druidDataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(druidDataSource);
        sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/mybatisConfig/tester/config.xml"));
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/tester/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }
    @Bean("testerSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("testerSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        SqlSessionTemplate sqlSessionTemplate=new SqlSessionTemplate(sqlSessionFactory);
        return  sqlSessionTemplate;
    }
    @Bean("testerTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("testerDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
