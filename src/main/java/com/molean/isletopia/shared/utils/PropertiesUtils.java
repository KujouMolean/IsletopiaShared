package com.molean.isletopia.shared.utils;

import com.molean.isletopia.shared.database.DataSourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
    private static final Map<String, Properties> propertiesMap = new HashMap<>();


    public static Properties getProperties(String name) {
        if (propertiesMap.containsKey(name)) {
            return propertiesMap.get(name);
        }
        Properties properties = null;
        try {
            InputStream inputStream = DataSourceUtils.class.getClassLoader().getResourceAsStream(name + ".properties");
            properties = new Properties();
            properties.load(inputStream);
            propertiesMap.put(name, properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
