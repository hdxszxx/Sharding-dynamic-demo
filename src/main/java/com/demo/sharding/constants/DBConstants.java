package com.demo.sharding.constants;

/**
 * @version 1.0
 * @date 2023/3/2 12:02
 */
public class DBConstants {
    /**
     * 数据源分组 - fast_cloud 主库
     * 这里的tour是yml中的 spring.datasource.dynamic.datasource.master
     */
    public static final String MASTER = "master";

    /**
     * 数据源分组 - fast_cloud 从库
     * 这里的avalon是yml中的 spring.datasource.dynamic.datasource.slave
     */
    public static final String SLAVE = "slave";

    /**
     * 数据源分组 - 分库分表
     */
    public static final String SHARDING = "sharding";
}
