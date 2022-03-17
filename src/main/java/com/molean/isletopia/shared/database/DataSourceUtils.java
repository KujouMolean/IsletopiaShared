package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.utils.PropertiesUtils;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataSourceUtils {
    private static final Map<String, MysqlConnectionPoolDataSource> dataSourceMap = new HashMap<>();

    public static Connection getConnection() {
        return getConnection("minecraft");
    }

    public static Connection getConnection(String server) {
        if (!dataSourceMap.containsKey(server)) {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();


            String url = PropertiesUtils.getProperties("config").getProperty("mysql.url").replace("%server%", server);
            String username = PropertiesUtils.getProperties("config").getProperty("mysql.username");
            String password = PropertiesUtils.getProperties("config").getProperty("mysql.password");
            dataSource.setUrl(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);

            //validate
//            dataSource.setTimeout(30);
//            dataSource.setValidationQuery("select 1");

            //timeout
//            dataSource.setMaxWait(30000);
//            dataSource.setQueryTimeout(30);
//            dataSource.setKillWhenSocketReadTimeout(true);

            //max active
//            dataSource.setMaxActive(64);

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
