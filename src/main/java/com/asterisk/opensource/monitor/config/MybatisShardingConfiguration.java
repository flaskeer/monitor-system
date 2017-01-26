package com.asterisk.opensource.monitor.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.asterisk.opensource.monitor.sharding.DBSharding;
import com.asterisk.opensource.monitor.sharding.TableSharding;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;


@org.springframework.context.annotation.Configuration
@MapperScan("com.asterisk.opensource.monitor.dao")
@EnableTransactionManagement
@Slf4j
public class MybatisShardingConfiguration {

    @Bean
    public DataSourceRule dataSourceRule(){
        return new DataSourceRule(createDataSource());
    }

    @Bean
    public DBSharding dbSharding(){
        return new DBSharding();
    }

    @Bean
    public TableSharding tableSharding(){
        return new TableSharding();
    }

    @Bean
    public DatabaseShardingStrategy databaseShardingStrategy(){
        return new DatabaseShardingStrategy("id",dbSharding());
    }

    @Bean
    public TableShardingStrategy tableShardingStrategy(){
        return new TableShardingStrategy("id",tableSharding());
    }

    @Bean
    public ShardingRule shardingRule(){
        //表分库分表规则
        TableRule goodsTr = TableRule.builder("goods")
                .actualTables(Arrays.asList("goods_0","goods_1"))
                .dataSourceRule(dataSourceRule())
                .build();
        return ShardingRule.builder()
                .dataSourceRule(dataSourceRule())
                .tableRules(Collections.singletonList(goodsTr))
                .databaseShardingStrategy(databaseShardingStrategy())
                .tableShardingStrategy(tableShardingStrategy())
                .build();
    }

    @Bean
    public ShardingDataSource shardingDataSource(){
        return new ShardingDataSource(shardingRule());
    }




    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setConfiguration(configuration());
        sqlSessionFactory.setTypeAliasesPackage("com.asterisk.opensource.monitor.domain");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
        return sqlSessionFactory.getObject();
    }





    @Bean
    public Configuration configuration() {
        Configuration configuration = new Configuration();
        configuration.setCacheEnabled(true);
        configuration.setUseGeneratedKeys(true);
        configuration.setDefaultExecutorType(ExecutorType.REUSE);
        configuration.setLazyLoadingEnabled(true);
        configuration.setDefaultStatementTimeout(5000);
        configuration.setMapUnderscoreToCamelCase(true);
        return configuration;
    }


    @Bean
    public Map<String,DataSource> createDataSource() {
        Map<String,DataSource> dataSourceMap = Maps.newConcurrentMap();
        dataSourceMap.put("ds_0",druidDataSource0());
        dataSourceMap.put("ds_1",druidDataSource1());
        return dataSourceMap;
    }


    @Value("${datasource0.driverClassName}")
    private String driver;
    @Value("${datasource0.url}")
    private String url;
    @Value("${datasource0.username}")
    private String username;
    @Value("${datasource0.password}")
    private String password;

    @Value("${datasource1.driverClassName}")
    private String driver1;
    @Value("${datasource1.url}")
    private String url1;
    @Value("${datasource1.username}")
    private String username1;
    @Value("${datasource1.password}")
    private String password1;

    @Bean(initMethod = "init",destroyMethod = "close")
    @Primary
    public DataSource druidDataSource0() {
        return getDataSource(driver, url, username, password);
    }

    private DataSource getDataSource(String driver, String url, String username, String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }


    @Bean(initMethod = "init",destroyMethod = "close")
    public DataSource druidDataSource1() {
        return getDataSource(driver1, url1, username1, password1);
    }


}
