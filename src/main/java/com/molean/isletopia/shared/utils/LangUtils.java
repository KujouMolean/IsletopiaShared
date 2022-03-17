package com.molean.isletopia.shared.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LangUtils {
    private static JsonObject defaultJsonObject = null;

    private static final Map<Locale, JsonObject> jsonObjectMap = new HashMap<>();

    static {
        InputStream inputStream = LangUtils.class.getClassLoader().getResourceAsStream("lang/zh_cn.json");
        if (inputStream != null) {
            @SuppressWarnings("all")
            JsonElement parse = new JsonParser().parse(new InputStreamReader(inputStream));
            defaultJsonObject = parse.getAsJsonObject();
        }
    }

    private static JsonObject get(Locale locale) {

        if (jsonObjectMap.containsKey(locale)) {
            JsonObject jsonObject = jsonObjectMap.get(locale);
            if (jsonObject == null) {
                return defaultJsonObject;
            }else{
                return jsonObject;

            }
        }

        InputStream inputStream = LangUtils.class.getClassLoader().getResourceAsStream("lang/" + locale.toString().toLowerCase(Locale.ROOT) + ".json");
        if (inputStream != null) {
            @SuppressWarnings("all")
            JsonElement parse = new JsonParser().parse(new InputStreamReader(inputStream));
            JsonObject asJsonObject = parse.getAsJsonObject();
            jsonObjectMap.put(locale, asJsonObject);
            return asJsonObject;
        }else{
            return defaultJsonObject;
        }
    }

    public static String get(Locale locale, String key) {
        String str = null;
        try {
            JsonElement jsonElement = get(locale).get(key);
            str = jsonElement.getAsString();
        } catch (Exception ignore) {
            str = key;
        }
        return str;
    }

    public static boolean contains(Locale locale, String key) {
        return !get(locale, key).equals(key);
    }


}
