package com.molean.isletopia.shared.parameter;

import com.molean.isletopia.shared.database.ParameterDao;

import java.util.List;

public class CustomParameter {
    public static void set(String namespace, String key, String value) {
        ParameterDao.set("custom", namespace, key, value);
    }

    public static String get(String namespace, String key) {
        return ParameterDao.get("custom", namespace, key);
    }

    public static void unset(String namespace, String key) {
        ParameterDao.delete("custom", namespace, key);
    }

    public static List<String> keys(String namespace) {
        List<String> contact = ParameterDao.keys("custom", namespace);
        return contact;
    }

}
