package com.demo.sharding.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidFilterConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidSpringAopConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidStatViewServletConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidWebStatFilterConfiguration;
import com.alibaba.druid.util.Utils;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.demo.sharding.constants.DBConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 动态数据源配置：
 *
 * 使用{@link com.baomidou.dynamic.datasource.annotation.DS}注解，切换数据源
 *
 * <code>@DS(DataSourceConfiguration.SHARDING_DATA_SOURCE_NAME)</code>
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class})
@Import({DruidSpringAopConfiguration.class,
        DruidStatViewServletConfiguration.class,
        DruidWebStatFilterConfiguration.class,
        DruidFilterConfiguration.class
}
)
public class DruidShardingJdbcDataSourceConfiguration {

    //动态数据源配置项
    @Autowired
    private DynamicDataSourceProperties properties;

    /**
     * shardingjdbc有四种数据源，需要根据业务注入不同的数据源
     *
     * <p>1. 未使用分片, 脱敏的名称(默认): shardingDataSource;
     * <p>2. 主从数据源: masterSlaveDataSource;
     * <p>3. 脱敏数据源：encryptDataSource;
     * <p>4. 影子数据源：shadowDataSource
     */
    @Lazy
    @Autowired
    DataSource shardingDataSource;

    /**
     * 不使用下列配置原因为旧版Dynamic（3.1.1）会造成重复加载数据源
     * 因为 @AutoConfigureBefore 该注解会变得无效 可参考该文章 https://blog.csdn.net/LiZhen314/article/details/125759730
     */
//    @Bean
//    public DynamicDataSourceProvider dynamicDataSourceProvider() {
//        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
//        return new AbstractDataSourceProvider() {
//            @Override
//            public Map<String, DataSource> loadDataSources() {
//                Map<String, DataSource> dataSourceMap = createDataSourceMap(datasourceMap);
//                // 将 shardingjdbc 管理的数据源也交给动态数据源管理
//                dataSourceMap.put(SHARDING_DATA_SOURCE_NAME, shardingDataSource);
//                return dataSourceMap;
//            }
//        };
//    }

    /**
     * 将 sharding 数据加入到 Dynamic 进行管理
     * @return
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        dataSource.addDataSource(DBConstants.SHARDING, shardingDataSource);
        return dataSource;
    }

    // 去除druid 监控界面广告
    @Bean
    public FilterRegistrationBean removeDruidAdFilter() throws IOException {
        // 获取common.js内容
        String text = Utils.readFromResource("support/http/resources/js/common.js");
        // 屏蔽 this.buildFooter(); 直接替换为空字符串,让js没机会调用
        final String newJs = text.replace("this.buildFooter();", "");
        // 新建一个过滤器注册器对象
        FilterRegistrationBean<javax.servlet.Filter> registration = new FilterRegistrationBean<>();
        // 注册common.js文件的过滤器
        registration.addUrlPatterns("/druid/js/common.js");
        // 添加一个匿名的过滤器对象,并把改造过的common.js文件内容写入到浏览器
        registration.setFilter((servletRequest, servletResponse, filterChain) -> {
            // 重置缓冲区，响应头不会被重置
            servletResponse.resetBuffer();
            // 把改造过的common.js文件内容写入到浏览器
            servletResponse.getWriter().write(newJs);
        });
        return registration;
    }
}