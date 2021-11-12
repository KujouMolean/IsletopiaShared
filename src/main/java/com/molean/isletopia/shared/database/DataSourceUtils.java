package com.molean.isletopia.shared.database;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataSourceUtils {
    private static final Map<String, DruidDataSource> dataSourceMap = new HashMap<>();

    public static Connection getConnection() {
        return getConnection("minecraft");
    }

    public static Connection getConnection(String server) {
        if (!dataSourceMap.containsKey(server)) {
            DruidDataSource dataSource = new DruidDataSource();
            Properties properties = null;
            try {
                InputStream inputStream = DataSourceUtils.class.getClassLoader().getResourceAsStream("mysql.properties");
                properties = new Properties();
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String url = properties.getProperty("url").replace("%server%", server);
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            //validate
            dataSource.setValidationQueryTimeout(30);
            dataSource.setValidationQuery("select 1");

            //timeout
            dataSource.setMaxWait(30000);
            dataSource.setQueryTimeout(30);
            dataSource.setKillWhenSocketReadTimeout(true);

            //max active
            dataSource.setMaxActive(64);

            dataSourceMap.put(server, dataSource);
        }
        Connection connection = null;
        try {
            connection = dataSourceMap.get(server).getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
}
