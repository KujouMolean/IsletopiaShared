package com.molean.isletopia.shared.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;

public class LangUtils {
    private static JsonObject jsonObject = null;

    static {
        InputStream inputStream = LangUtils.class.getClassLoader().getResourceAsStream("zh_cn.json");
        if (inputStream != null) {
            @SuppressWarnings("all")
            JsonElement parse = new JsonParser().parse(new InputStreamReader(inputStream));
            jsonObject = parse.getAsJsonObject();
        }
    }

    public static String get(String key) {
        String str = null;
        try {
            JsonElement jsonElement = jsonObject.get(key);
            str = jsonElement.getAsString();
        } catch (Exception ignore) {
            str = key;
        }
        return str;
    }


}
